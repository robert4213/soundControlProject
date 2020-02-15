package com.project.app;

import java.io.*;

public class ButtonCommand{
    public String buttonRead(){
        String s = "";
        String str = "";
        try {                
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("python input.py");
            // Process p = Runtime.getRuntime().exec("ls");
            
            BufferedReader stdInput = new BufferedReader(new 
                InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                InputStreamReader(p.getErrorStream()));

            // read the output from the command
            while ((str = stdInput.readLine()) != null) {
                System.out.println(str);
                s = str;
            }            
        }catch (Exception e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            // System.exit(-1);
        }
        return s;
    }
}