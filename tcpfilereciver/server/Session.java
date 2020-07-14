package tcpfilereciver.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

class Session implements Runnable
{

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private static final long timeout = 3000;
    private String uploads = "uploads";
    private File f;

    Session(Socket _socket) throws IOException
    {
        socket = _socket;
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run()
    {

        try
        {
            String filename = readFileName();

            f = createNotExistedFile(filename);

            try (FileOutputStream fout = new FileOutputStream(f))
            {
                long fileLength = input.readLong();
                recvFile(fileLength, fout);
            }
            catch (IOException e)
            {
                System.out.println("Error while receiving file:" + e.getMessage());

                sendFinalMessage("Failed!");
                return;
            }

            sendFinalMessage("Success!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }


    private String getSpeedMess(double speed)
    {
        if(speed <= 1024)                    { return String.format("%8.2f  B/s|", speed); }
        else if(speed <= 1024 * 1024)        { return String.format("%8.2f KB/s|", speed/1024); }
        else if(speed <= 1024 * 1024 * 1024) { return String.format("%8.2f MB/s|", speed/1024/1024);}
        else                                 { return String.format("%8.2f GB/s|", speed/1024/1024/1024);}
    }

    private void proccedSpeed(long fileLength, long readAll, long readNow, long startTime, long prevShowTime)
    {
        long timeNow = System.currentTimeMillis();

        double speed = (double)(readAll)/(timeNow - startTime)*1000;
        double instantSpeed = (double)(readNow)/(timeNow - prevShowTime)*1000;
        String mess = String.format("|%-15s|", socket.getInetAddress());
        mess += getSpeedMess(speed);
        mess += getSpeedMess(instantSpeed);
        mess += String.format("%6.2f %%|", (double)readAll/fileLength*100);
        mess += String.format("%s|", f.getName());

        System.out.println(mess);
    }

    private void recvFile(long length, FileOutputStream fout) throws IOException
    {
        int buffSize = 1024;
        byte[] buffer = new byte[buffSize];

        long readAll = 0;
        long startTime = System.currentTimeMillis();
        long prevShowTime = System.currentTimeMillis() - 1000;

        long readWithOneIter = 0;
        while(readAll < length)
        {

            proccedSpeed(length, readAll, readWithOneIter, startTime, prevShowTime);

            readWithOneIter = 0;
            prevShowTime = System.currentTimeMillis();

            try
            {
                long currStartTime = System.currentTimeMillis()
                        ,currEndTime = currStartTime;

                do
                {
                    socket.setSoTimeout((int)timeout - (int)(currEndTime - currStartTime));

                    long readNow;
                    if(length - readAll >= buffSize)
                    {
                        readNow = input.read(buffer);
                    }
                    else
                    {
                        readNow = input.read(buffer, 0, (int)(length - readAll));
                    }

                    fout.write(buffer, 0, (int)readNow);
                    readWithOneIter += readNow;
                    readAll += readNow;
                    currEndTime = System.currentTimeMillis();
                }
                while(currEndTime - currStartTime < timeout && readAll < length);

            }
            catch (SocketTimeoutException ignored) { }


        }
        proccedSpeed(length, length, 0, 0, 0);

    }

    private String readFileName() throws IOException
    {
        int nameLength = input.readInt();

        byte[] nameBytes = new byte[nameLength];

        input.readFully(nameBytes, 0, nameLength);

        return new String(nameBytes, StandardCharsets.UTF_8);
    }
    private File createNotExistedFile(String filename)
    {
        int slashPos = filename.lastIndexOf("/");
        if(slashPos != -1)
        {
            filename = filename.substring(slashPos);
        }

        int invertSlashPos = filename.lastIndexOf("\\");
        if(invertSlashPos != -1)
        {
            filename = filename.substring(invertSlashPos);
        }

        String prefix = filename;
        String postfix = "";
        int dotPos = filename.lastIndexOf('.');
        if(dotPos != -1)
        {
            prefix = filename.substring(0, dotPos);
            postfix = filename.substring(dotPos);
        }

        File f;

        int i = 0;
        while(true)
        {
            String newFilename = prefix + postfix;
            if(i != 0)
            {
                newFilename = prefix + "(" + i + ")" + postfix;
            }
            f = new File(uploads + "/" + newFilename);

            if(!f.exists())
            {
                return f;
            }
            ++i;
        }
    }
    private void sendFinalMessage(String mess) throws IOException
    {
        byte[] messByte = mess.getBytes(StandardCharsets.UTF_8);
        output.writeInt(messByte.length);
        output.write(messByte);
    }

}
