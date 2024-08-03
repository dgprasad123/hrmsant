/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller;

import hrms.common.Util;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Manoj PC
 */

public class CaptchaGenServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
             public static final String FILE_TYPE = "jpeg";
 
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
 
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Max-Age", 0);
 
            String captchaStr="";
 
        //captchaStr=Util.generateCaptchaTextMethod();
 
            captchaStr=Util.generateCaptchaTextMethod2(6);
            OutputStream outputStream=null;
            try {
 
                int width=130;      int height=40;
 
                Color bg = new Color(62,139,214);
                Color fg = new Color(255,255,255);
 
                Font font = new Font("Arial", Font.BOLD, 25);
                BufferedImage cpimg =new BufferedImage(width,height,BufferedImage.OPAQUE);
                Graphics g = cpimg.createGraphics();
 
                g.setFont(font);
                g.setColor(bg);
                g.fillRect(0, 0, width, height);
                g.setColor(fg);
                g.drawString(captchaStr,10,25);   
 
                HttpSession session = request.getSession(true);
                session.setAttribute("CAPTCHA", captchaStr);
 
               outputStream = response.getOutputStream();
 
               ImageIO.write(cpimg, FILE_TYPE, outputStream);
               outputStream.close();
 
            } catch (Exception e) {
                    e.printStackTrace();
            }finally{
                outputStream.close();
                outputStream.flush();
            }
        }
 
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
            doPost(request, response);
        }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    

}
