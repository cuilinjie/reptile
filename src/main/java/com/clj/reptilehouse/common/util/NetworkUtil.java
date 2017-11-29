
package com.clj.reptilehouse.common.util;

import java.net.Inet4Address;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public abstract class NetworkUtil
{

    public NetworkUtil()
    {
    }
    
    //转换十进制掩码为IP地址格式掩码
    public static final String getMask(byte maskBit) {
        if(maskBit == 1)
            return "128.0.0.0";
        else if(maskBit == 2)
            return "192.0.0.0";
        else if(maskBit == 3)
            return "224.0.0.0";
        else if(maskBit == 4)
            return "240.0.0.0";
        else if(maskBit == 5)
            return "248.0.0.0";
        else if(maskBit == 6) 
            return "252.0.0.0";
        else if(maskBit == 7)
            return "254.0.0.0";
        else if(maskBit == 8)
            return "255.0.0.0";
        else if(maskBit ==9)
            return "255.128.0.0";
        else if(maskBit == 10)
            return "255.192.0.0";
        else if(maskBit == 11)
            return "255.224.0.0";
        else if(maskBit == 12)
            return "255.240.0.0";
        else if(maskBit == 13)
            return "255.248.0.0";
        else if(maskBit == 14)
            return "255.252.0.0";
        else if(maskBit == 15)
            return "255.254.0.0";
        else if(maskBit == 16)
            return "255.255.0.0";
        else if(maskBit == 17)
            return "255.255.128.0";
        else if(maskBit == 18)
            return "255.255.192.0";
        else if(maskBit == 19)
            return "255.255.224.0";
        else if(maskBit == 20)
            return "255.255.240.0";
        else if(maskBit == 21)
            return "255.255.248.0";
        else if(maskBit == 22)
            return "255.255.252.0";
        else if(maskBit == 23)
            return "255.255.254.0";
        else if(maskBit == 24)
            return "255.255.255.0";
        else if(maskBit == 25)
            return "255.255.255.128";
        else if(maskBit == 26)
            return "255.255.255.192";
        else if(maskBit == 27)
            return "255.255.255.224";
        else if(maskBit == 28)
            return "255.255.255.240";
        else if(maskBit == 29)
            return "255.255.255.248";
        else if(maskBit == 30)
            return "255.255.255.252";
        else if(maskBit == 31)
            return "255.255.255.254";
        else if(maskBit == 32)
            return "255.255.255.255";
        return "";
    }
    //判断mac是否合法
    public static final boolean isValidMAC(String mac){
    	 boolean flag=false;
    	 String patternMac="^[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}$";
         Pattern pa= Pattern.compile(patternMac);
         flag=pa.matcher(mac).find();
    	 return flag;
    }
    
    //判断IP是否合法
    public static final boolean isValidIP(String ip) {
        if(ip.indexOf(".") == -1)
            return false;
        String[] ipSplit = ip.split("\\.");
        int ipNum = 0;
        if (ipSplit.length != 4)
            return false;
        for (int i = 0; i < ipSplit.length; i++) {
            try {
                ipNum = Integer.parseInt(ipSplit[i]);
            }catch(Exception e) {
                return false;
            }
            if(ipNum < 0 || ipNum > 255)
                return false;
            if(i == 0)
                if(ipNum == 0 || ipNum == 255)
                return false;
        }
        return true;
    }
    
    //判断子网掩码是否合法
    public static final boolean isValidMask(String mask) {
        int maskNum = 0;
        int maskBit = 0;
        //十进制掩码
        if(mask.indexOf(".") == -1) {
            try {
                maskBit = Byte.parseByte(mask);
            }catch(Exception e) {
                return false;
            }
            if(maskBit > 31 || maskBit < 1) {
                return false;
            }
            return true;
        }
        //IP格式掩码
        String[] maskSplit = mask.split("\\.");
        String maskBinString = "";
        if(maskSplit.length != 4) 
            return false;
        //将大于128的4个掩码段连成2进制字符串
        for(int i=0; i<maskSplit.length; i++) {
            try {
                maskNum = Integer.parseInt(maskSplit[i]);
            }catch(Exception e) {
                return false;
            }
            //首位为0，非法掩码
            if(i == 0 && Integer.numberOfLeadingZeros(maskNum) == 32)
                return false;
            //非0或128～255之间，非法掩码
            if(Integer.numberOfLeadingZeros(maskNum) != 24)
                if(Integer.numberOfLeadingZeros(maskNum) != 32)
                    return false;
            //将大于128的掩码段连接成完整的二进制字符串
            maskBinString = maskBinString.concat(Integer.toBinaryString(maskNum));
        }
        //二进制掩码字符串，包含非连续1时，非法掩码
        if(maskBinString.indexOf("0") < maskBinString.lastIndexOf("1"))
                return false;
        //剩下的就是合法掩码
        return true;
    }

    public static final boolean isNumeric(String str){
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static final long to_number(byte p[], int off, int len)
    {
        long ret = 0L;
        int done = off + len;
        if(len != 1 && p.length >= done)
        {
            for(int i = off; i < done; i++)
                ret = (ret << 8 & -1L) + (long)(p[i] & 0xff);
        } else
        {
            if(p.length > off)
            {
                byte b = p[off];
                ret = b & 0xff;
            }
        }
        return ret;
    }

    public static final short toShort(byte buffer[], int pos)
    {
        return (short)(buffer[pos] << 8 | buffer[pos + 1] & 0xff);
    }

    public static final char toUShort(byte buffer[], int pos)
    {
        return (char)toShort(buffer, pos);
    }

    public static final long toUInt(byte buffer[], int pos)
    {
        return (long)toInt(buffer, pos) & 0xffffffffL;
    }

    public static final int toInt(byte buffer[], int pos)
    {
        return buffer[pos] << 24 | (buffer[pos + 1] & 0xff) << 16 | (buffer[pos + 2] & 0xff) << 8 | buffer[pos + 3] & 0xff;
    }

    public static final long toLong(byte buffer[], int pos)
    {
        return toUInt(buffer, pos) << 32 | toUInt(buffer, pos + 4);
    }

    public static final long toLong(byte buffer[], int pos, int length)
    {
        long val = 0L;
        for(int i = 0; i < length; i++)
            val = val << 8 | (long)buffer[pos + i] & 255L;

        return val;
    }

    public static final int toInt(Inet4Address address)
    {
        return address.hashCode();
    }

    public static int toInt(String address)
    {
        int value = 0;
        int blockValue = 0;
        int dotCount = 0;
        int l = address.length();
        for(int i = 0; i <= l; i++)
        {
            if(i == l || address.charAt(i) == '.')
            {
                if(blockValue > 255)
                    throw new IllegalArgumentException("Invalid value [" + blockValue + "]");
                dotCount++;
                value <<= 8;
                value += blockValue;
                blockValue = 0;
                continue;
            }
            blockValue *= 10;
            int v = Character.digit(address.charAt(i), 10);
            if(v == -1)
                throw new IllegalArgumentException("Invalid character [" + address.charAt(i) + "]");
            blockValue += v;
        }

        if(dotCount != 4)
            throw new IllegalArgumentException("Incorrect amount of decimal points [" + (dotCount - 1) + "]");
        else
            return value;
    }

    public static String ellipsis(String string, int len)
    {
        if(string.length() <= len)
            return string;
        else
            return string.substring(0, len - 3) + "...";
    }

    public static StringBuffer appendSentenceCase(StringBuffer sb, String str)
    {
        if(str.length() > 0)
        {
            sb.append(str);
            int ix = sb.length() - str.length();
            sb.setCharAt(ix, Character.toUpperCase(sb.charAt(ix)));
        }
        return sb;
    }

    public static String toSentenceCase(String str)
    {
        if(str.length() > 0 && Character.isLowerCase(str.charAt(0)))
            return appendSentenceCase(new StringBuffer(str.length()), str).toString();
        else
            return str;
    }

    public static final long getValue(byte buffer[], int offset, int fieldLength, int maxLengthBits)
    {
        if(fieldLength == 0)
            return 0L;
        if(fieldLength * 8 > maxLengthBits)
        {
            int newFieldLength = (maxLengthBits + 7) / 8;
            offset += fieldLength - newFieldLength;
            fieldLength = newFieldLength;
        }
        return toLong(buffer, offset, fieldLength) & (1L << maxLengthBits) - 1L;
    }

    public static final long ciscosamplerate(byte p[], int off, int len)
    {
        long ret = 0L;
        int done = off + len;
        for(int i = off; i < done; i++)
        {
            if(i == off)
            {
               ret = (ret << 8 & -1L) + (long)((p[i] & 63L) & 0xff);
            } else
               ret = (ret << 8 & -1L) + (long)(p[i] & 0xff);
        }

        return ret;
    }
    
    
    public static final String to_string(String intaddr)
    {
        long result = 0L;
        if(intaddr != null)
        {
            String addr[] = intaddr.split("\\.");
            int i = 0;
            for(int j = addr.length - 1; i < addr.length - 1; j--)
            {
                result = (long)((double)result + (double)Integer.parseInt(addr[i]) * Math.pow(256D, j));
                i++;
            }

            result += Integer.parseInt(addr[addr.length - 1]);
        }
        String res = (new Long(result)).toString();
        return res;
    }

    private static final String value(long num, String msg)
    {
        if(num == 0L)
            return "";
        else
            return (num != 1L ? num + " " + msg + "s" : "1 " + msg) + ", ";
    }

    public static final String uptime(long time)
    {
        if(time == 0L)
            return "0 seconds";
        if(time < 0L)
        {
            return time + "(Negative?!)";
        } else
        {
            long sec = time % 60L;
            long min = (time / 60L) % 60L;
            long hour = (time / 60L / 60L) % 24L;
            long day = time / 60L / 60L / 24L;
            String ret = value(day, "day") + value(hour, "hour") + value(min, "minute") + value(sec, "second");
            return ret.substring(0, ret.length() - 2);
        }
    }

    private static final String value1(long l)
    {
        return "" + DIGITS[(int)(l / 10L) % 10] + DIGITS[(int)l % 10];
    }

    public static final String uptime_short(long time)
    {
        if(time == 0L)
            return "00:00";
        if(time < 0L)
        {
            return time + "(Negative?!)";
        } else
        {
            long sec = time % 60L;
            long min = (time / 60L) % 60L;
            long hour = (time / 60L / 60L) % 24L;
            long day = time / 60L / 60L / 24L;
            return value1(day) + '-' + value1(hour) + ':' + value1(min) + ':' + value1(sec);
        }
    }

    public static final String str_addr(long addr)
    {
        return new String((addr >> 24 & 255L) + "." + (addr >> 16 & 255L) + "." + (addr >> 8 & 255L) + "." + (addr & 255L));
    }

    public static final String toInterval(long i)
    {
        if(i < 60L)
            return i + "S";
        if(i < 3600L)
            return i / 60L + "M";
        else
            return i / 3600L + "H";
    }

    public static final byte[] Convert32LongtoByte4(long numbers)
    {
                byte[] result = new byte[4];

                result[3] = (byte) (numbers & 0xffL);
                result[2] = (byte) ((numbers >>> 8) & 0xffL);
                result[1] = (byte) ((numbers >>> 16) & 0xffL);
                result[0] = (byte) ((numbers >>> 24) & 0xffL);

                return result;
    }

    public static final byte[] Convert16InttoByte(int numbers)
    {
                byte[] result = new byte[2];
                result[1] = (byte) (numbers & 0xffL);
                result[0] = (byte) ((numbers >>> 8) & 0xffL);
                return result;
    }

    /*public static final long ip2long(String intaddr)
    {
        long result = 0L;
        if(intaddr != null)
        {
            String addr[] = intaddr.split("\\.");
            int i = 0;
            for(int j = addr.length - 1; i < addr.length - 1; j--)
            {
                result = (long)((double)result + (double)Integer.parseInt(addr[i]) * Math.pow(256D, j));
                i++;
            }

            result += Integer.parseInt(addr[addr.length - 1]);
        }
        return result;
    }*/

    public static final long ip2long(String intaddr)
    {
      long result = 0L;
      if(intaddr != null)
      {
          String addr[] = new String[4];
          StringTokenizer st = new StringTokenizer (intaddr, ".");
          int count = st.countTokens();
          if(count == 4)
          {
              int i = 0;
              while (st.hasMoreTokens()) {
                  String token = st.nextToken();
                  token = token.trim();
                  if (i >= 4)
                      break;
                  if (token != null) {
                      addr[i] = new String(token.toString());
                  }
                  i++;
              }
              result += (long) Integer.parseInt(addr[0]) << 24;
              result += (long) Integer.parseInt(addr[1]) << 16;
              result += (long) Integer.parseInt(addr[2]) << 8;
              result += (long) Integer.parseInt(addr[3]);
          }
      }
      return result;
    }

    public static final long ip2longtest(String intaddr)
    {
      long result = 0L;
      if(intaddr != null)
      {
          String addr[] = intaddr.split("\\.");
          result += (long) Integer.parseInt(addr[0]) << 24;
          result += (long) Integer.parseInt(addr[1]) << 16;
          result += (long) Integer.parseInt(addr[2]) << 8;
          result += (long) Integer.parseInt(addr[3]);
      }
      return result;
    }

    public static final int getMask(String mask)
    {
       String splitmask[] = mask.split("\\.");
       int masks = 0;
       if(splitmask.length == 4)
       {
          int mask1 = Integer.parseInt(splitmask[0]);
          int mask2 = Integer.parseInt(splitmask[1]);
          int mask3 = Integer.parseInt(splitmask[2]);
          int mask4 = Integer.parseInt(splitmask[3]);

          String maskbin1 = Integer.toBinaryString(mask1);
          String maskbin2 = Integer.toBinaryString(mask2);
          String maskbin3 = Integer.toBinaryString(mask3);
          String maskbin4 = Integer.toBinaryString(mask4);

          for(int i = 0; i < maskbin1.length(); i++)
          {
                masks += Integer.parseInt(String.valueOf(maskbin1.charAt(i)));
          }

          for(int i = 0; i < maskbin2.length(); i++)
          {
                masks += Integer.parseInt(String.valueOf(maskbin2.charAt(i)));
          }

          for(int i = 0; i < maskbin3.length(); i++)
          {
                masks += Integer.parseInt(String.valueOf(maskbin3.charAt(i)));
          }

          for(int i = 0; i < maskbin4.length(); i++)
          {
                masks += Integer.parseInt(String.valueOf(maskbin4.charAt(i)));
          }
       }
       return masks;
    }

    public static final long getBroadCastlong(long iplong, int mask)
    {
        long broadcastlong = 0L;

        long prefix = (long) ((iplong >> (32 - mask)) << (32 - mask));

        String subnetstr = str_addr(prefix);
        long masklong = getNetworkMask(mask);
        String maskstr = str_addr(masklong);

        String split1[] = subnetstr.split("\\.");
        String split2[] = maskstr.split("\\.");

        int value[] = new int[4];
        value[0] = 255 - Integer.parseInt(split2[0]);
        value[1] = 255 - Integer.parseInt(split2[1]);
        value[2] = 255 - Integer.parseInt(split2[2]);
        value[3] = 255 - Integer.parseInt(split2[3]);

        int bcastint[] = new int[4];
        bcastint[0] = Integer.parseInt(split1[0]) + value[0];
        bcastint[1] = Integer.parseInt(split1[1]) + value[1];
        bcastint[2] = Integer.parseInt(split1[2]) + value[2];
        bcastint[3] = Integer.parseInt(split1[3]) + value[3];

        String bcaststr = String.valueOf(bcastint[0]) + "." + String.valueOf(bcastint[1]) + "." + String.valueOf(bcastint[2]) + "." + String.valueOf(bcastint[3]);

        broadcastlong = ip2long(bcaststr);

        return broadcastlong;
    }

    public static final String getBroadCastString(long iplong, String mask)
    {
        int maskint = getMask(mask);
        long subnetlong = (iplong >> (32 - maskint)) << (32 - maskint);
        String subnetstr = str_addr(subnetlong);

        String split1[] = subnetstr.split("\\.");
        String split2[] = mask.split("\\.");

        int value[] = new int[4];
        value[0] = 255 - Integer.parseInt(split2[0]);
        value[1] = 255 - Integer.parseInt(split2[1]);
        value[2] = 255 - Integer.parseInt(split2[2]);
        value[3] = 255 - Integer.parseInt(split2[3]);

        int bcastint[] = new int[4];
        bcastint[0] = Integer.parseInt(split1[0]) + value[0];
        bcastint[1] = Integer.parseInt(split1[1]) + value[1];
        bcastint[2] = Integer.parseInt(split1[2]) + value[2];
        bcastint[3] = Integer.parseInt(split1[3]) + value[3];

        String bcaststr = String.valueOf(bcastint[0]) + "." + String.valueOf(bcastint[1]) + "." + String.valueOf(bcastint[2]) + "." + String.valueOf(bcastint[3]);

        return bcaststr;
    }

    public static final String getBroadcast(long iplong, int mask)
    {
        long subnetlong = (iplong >> (32 - mask)) << (32 - mask);
        String subnetstr = str_addr(subnetlong);
        long masklong = getNetworkMask(mask);
        String maskStr = str_addr(masklong);

        String split1[] = subnetstr.split("\\.");
        String split2[] = maskStr.split("\\.");

        int value[] = new int[4];
        value[0] = 255 - Integer.parseInt(split2[0]);
        value[1] = 255 - Integer.parseInt(split2[1]);
        value[2] = 255 - Integer.parseInt(split2[2]);
        value[3] = 255 - Integer.parseInt(split2[3]);

        int bcastint[] = new int[4];
        bcastint[0] = Integer.parseInt(split1[0]) + value[0];
        bcastint[1] = Integer.parseInt(split1[1]) + value[1];
        bcastint[2] = Integer.parseInt(split1[2]) + value[2];
        bcastint[3] = Integer.parseInt(split1[3]) + value[3];

        String bcaststr = String.valueOf(bcastint[0]) + "." + String.valueOf(bcastint[1]) + "." + String.valueOf(bcastint[2]) + "." + String.valueOf(bcastint[3]);

        return bcaststr;
    }

    // DDos Analysis - lfc debug delete version 5
    public static final boolean in_isBroadcastAddress(long address)
    {
       if(address == 0x0)
         return false; /* IP-less myGlobals.device (is it trying to boot via DHCP/BOOTP ?) */
       else {
         if(((address & 0x000000FF) == 0x000000FF) || ((address & 0x000000FF) == 0x00000000) /* Network address */ )
         {
                return true;
         }
         return false;
       }
    }

    public static final long getNetworkMask(int mask)
    {
        long masklong = 0L;

        int mask1 = 0;
        int mask2 = 0;
        int mask3 = 0;
        int mask4 = 0;

        if(mask >=0 && mask <= 8)
        {
            int value = 1;
            for(int i = 0; i < (8 - mask); i++)
                value *= 2;
            mask1 = 256 - value;
            mask2 = 0;
            mask3 = 0;
            mask4 = 0;
        } else
        if(mask > 8 && mask <= 16)
        {
            int value = 1;
            for(int i = 0; i < (16 - mask); i++)
                value *= 2;
            mask1 = 255;
            mask2 = 256 - value;
            mask3 = 0;
            mask4 = 0;
        } else
        if(mask > 16 && mask <= 24)
        {
            int value = 1;
            for(int i = 0; i < (24 - mask); i++)
                value *= 2;
            mask1 = 255;
            mask2 = 255;
            mask3 = 256 - value;
            mask4 = 0;
        } else
        if(mask > 24 && mask <= 32)
        {
            int value = 1;
            for(int i = 0; i < (32 - mask); i++)
                value *= 2;
            mask1 = 255;
            mask2 = 255;
            mask3 = 255;
            mask4 = 256 - value;
        }

        masklong += (long)mask1 << 24;
        masklong += (long)mask2 << 16;
        masklong += (long)mask3 << 8;
        masklong += (long)mask4;

        return masklong;
    }

   public static final boolean in_isMulticastAddress(long address)
   {
       if((address & 0xE0000000) == 0xE0000000)
       {
          return true;
       }
       else
          return false;
   }

   public static final long[] getNetworkRange(long lNet, long lNetmask)
   {
       lNet &= lNetmask;
       long nsize = 0xffffffffL ^ lNetmask;
       long range[] = {
           lNet, lNet + nsize
       };
       return range;
   }

   public static final String getHex( byte [] raw , int len) {
      if ( raw == null ) {
          return null;
      }
      final StringBuilder hex = new StringBuilder();
      for ( int i = 0; i < len; i++ ) {
         byte b = raw[i];
         hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
      }
      return hex.toString();
   }

   public static final byte[] hexStringToByteArray(String s) {
       byte[] b = new byte[s.length() / 2];
       for (int i = 0; i < b.length; i++) {
          int index = i * 2;
          int v = Integer.parseInt(s.substring(index, index + 2), 16);
          b[i] = (byte) v;
       }
       return b;
   }

   public static final long getLongMacAddress(String macaddr)
   {
       long macaddrlong = 0L;
       try
       {
           String splitter = "-";
           byte[] addr = new byte[6];
           if(macaddr != null && macaddr.indexOf(splitter) > 0)
           {
               String splits[] = macaddr.split(splitter);
               if(splits != null && splits.length == 6)
               {
                   String hexmacaddr = "";
                   for(int i = 0; i < splits.length; i++)
                   {
                       hexmacaddr += splits[i];
                   }
                   addr = hexStringToByteArray(hexmacaddr);
               }
           }
           if(addr != null && addr.length == 6)
           {
               String splits[] = macaddr.split("-");
               if (splits != null && splits.length == 6) {
                   macaddrlong = ((long) addr[5] & 0xff) +
                                 (((long) addr[4] & 0xff) << 8) +
                                 (((long) addr[3] & 0xff) << 16) +
                                 (((long) addr[2] & 0xff) << 24) +
                                 (((long) addr[1] & 0xff) << 32) +
                                 (((long) addr[0] & 0xff) << 40);
               }
           }
       }
       catch(Exception ex)
       {
           ex.printStackTrace();
       }
       return macaddrlong;
   }

   public static final String getStringMacAddress(long macaddr)
   {
       String macstr = "";
       String splitter = "-";

       byte addr[] = new byte[6];
       addr[0] = (byte) (macaddr >> 40);
       addr[1] = (byte) (macaddr >> 32);
       addr[2] = (byte) (macaddr >> 24);
       addr[3] = (byte) (macaddr >> 16);
       addr[4] = (byte) (macaddr >> 8);
       addr[5] = (byte) macaddr;

       String macaddrstr = getHex(addr, 6);
       if(macaddrstr != null && macaddrstr.length() == 12)
       {
           for(int i = 0; i < 6; i++)
           {
               if(i > 0)
                   macstr += splitter;
               macstr += macaddrstr.substring(i * 2, i * 2 + 2);
           }
       }
       return macstr;
   }

   public static final long getFreeSpaceOnLinux(String path)
   {
        try
        {
            long bytesFree = -1L;
            String buffer = "";
            String strFreeBytes = null;
            Process p = Runtime.getRuntime().exec((new StringBuilder()).append("/bin/df /").append(path).toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            buffer = reader.readLine();
            String header[] = null;
            for(; buffer != null; buffer = reader.readLine())
            {
                if(header == null)
                    header = buffer.split("\\s+");
                strFreeBytes = buffer;
            }

            for(int i = 0; i < header.length; i++)
            {
                System.out.println((new StringBuilder()).append("free space linux : ").append(header[i]).toString());
                if(header[i].startsWith("Avail"))
                {
                    String values[] = strFreeBytes.split("\\s+");
                    bytesFree = Long.parseLong(values[i]);
                    System.out.println((new StringBuilder()).append("free space linux bytesFree : ").append(bytesFree).toString());
                } else
                if(header[i].indexOf("blocks") != -1)
                {
                    String values[] = strFreeBytes.split("\\s+");
                    long totalDiskSpace = Long.parseLong(values[i]) * 1000L;
                    System.out.println((new StringBuilder()).append("free space linux totalDiskSpace : ").append(totalDiskSpace).toString());
                }
            }

            return bytesFree;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0L;
   }

   public static final int getFreePercentOnLinux(String path)
   {
        try
        {
            int percent = 0;
            String buffer = "";
            String strFreeBytes = null;
            Process p = Runtime.getRuntime().exec((new StringBuilder()).append("/bin/df /").append(path).toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            buffer = reader.readLine();
            String header[] = null;
            for(; buffer != null; buffer = reader.readLine())
            {
                if(header == null)
                    header = buffer.split("\\s+");
                strFreeBytes = buffer;
            }

            for(int i = 0; i < header.length; i++)
            {
                System.out.println((new StringBuilder()).append("free space linux : ").append(header[i]).toString());
                if(header[i].indexOf("blocks") != -1)
                {
                    String values[] = strFreeBytes.split("\\s+");
                    long totalDiskSpace = Long.parseLong(values[i]) * 1000L;
                    System.out.println((new StringBuilder()).append("free space linux totalDiskSpace : ").append(totalDiskSpace).toString());
                }
                if(header[i].indexOf("Use%") != -1)
                {
                    String values[] = strFreeBytes.split("\\s+");
                    if(values[i] != null && values[i].indexOf("%") > 0)
                    {
                           percent = Integer.parseInt(values[i].substring(0, values[i].indexOf("%")));
                    }
                    System.out.println("percent====" + percent);
                }
            }

            return percent;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

   private static final String HEXES = "0123456789ABCDEF";

   private static final char DIGITS[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
   
   public static void main(String[] args) {
	   /*String patternMac="^[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}+:[a-fA-F0-9]{2}$";
       Pattern pa= Pattern.compile(patternMac);
       System.out.println(pa.matcher("23:34:3e:5f:33:3d").find());*/
	   System.out.println(isSameSegment("218.241.144.134","255.255.255.240","218.241.144.134","255.255.255.240"));
   }

   // 获取IP地址的二进制的字符串式表达
	public static String getIPbinaryByString(String ipStr) {
		String binaryStr = "";
		String[] ipStrArr = ipStr.split("\\.");
		for (int i = 0; i < ipStrArr.length; i++) {
			String s = Integer.toBinaryString(Integer.parseInt(ipStrArr[i]));
			int sLen = s.length();
			if (sLen < 8) {
				for (int j = 0; j < (8 - sLen); j++) {
					s = "0".concat(s);
				}
			}
			binaryStr = binaryStr.concat(s);
		}
		return binaryStr;
	}

	// 根据子网掩码中1所占位数得到二进制的字符串式表达
	public static String getMaskBinaryByCount(String maskStr) {
		String binaryStr = "";
		int maskCount = 0;
		if (maskStr.length() > 2) {

			binaryStr = getIPbinaryByString(maskStr);

		} else {
			maskCount = Integer.parseInt(maskStr);

			for (int i = 0; i < maskCount; i++) {

				binaryStr = binaryStr.concat("1");
			}
			for (int i = 0; i < (32 - maskCount); i++) {

				binaryStr = binaryStr.concat("0");
			}
		}
		return binaryStr;
	}
	
		// 根据子网掩码得到位数 如：255.255.255.0为24位
		public static int getMaskCount(String maskStr) {
			int n=0;
			if(maskStr==null||maskStr==""){
				return n;
			}
			String[] maskList = { "128.0.0.0", "192.0.0.0", "224.0.0.0",
					"240.0.0.0", "248.0.0.0", "252.0.0.0", "254.0.0.0",
					"255.0.0.0", "255.128.0.0", "255.192.0.0", "255.224.0.0",
					"255.240.0.0", "255.248.0.0", "255.252.0.0", "255.254.0.0",
					"255.255.0.0", "255.255.128.0", "255.255.192.0",
					"255.255.224.0", "255.255.240.0", "255.255.248.0",
					"255.255.252.0", "255.255.254.0", "255.255.255.0",
					"255.255.255.128", "255.255.255.192", "255.255.255.224",
					"255.255.255.240", "255.255.255.248", "255.255.255.252",
					"255.255.255.254", "255.255.255.255" };
			for(int i=0;i<maskList.length;i++){
						if(maskStr.equals(maskList[i])){
							n=i+1;
							break;
						}
					}
			return n;
		}
		
	public static boolean isSameSegment(String ipStr,String ipMask,String ipSubStr,String ipSubMask){
		boolean flag=false;
		if(!trim(ipMask).equals(trim(ipSubMask)))
			return flag;
		if(getMaskCount(trim(ipMask))==0)
			return flag;
		String IPsubnetBinary = getIPbinaryByString(trim(ipStr));
		String IPmaskBinary = getMaskBinaryByCount(String.valueOf(getMaskCount(trim(ipMask))));
		String IPtestBinary = getIPbinaryByString(trim(ipSubStr));

		long maskIP = Long.parseLong(IPmaskBinary, 2); // 2进制
		long subnetIP = Long.parseLong(IPsubnetBinary, 2); // 2进制
		long IP = Long.parseLong(IPtestBinary, 2); // 2进制

		if ((maskIP & subnetIP) == (maskIP & IP)) {
			flag=true;
		}
		return flag;
	}
	
	
	/**
	 * 去掉指定字符串两端的空格
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 去掉两端空格后的字符串。如果传入的指定字符串是null，返回""。
	 */
	public static String trim(String value) {
		if (value == null) {
			return "";
		}
		else {
			return value.trim();
		}
	}
}
