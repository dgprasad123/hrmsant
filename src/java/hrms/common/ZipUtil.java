package hrms.common;

/**
 *
 * @author Surendra
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Surendra
 */
public class ZipUtil {

    

    public static void main(String[] args) {
        ZipUtil zip = new ZipUtil();
        //ZipUtil.createZip("D:\\ltaxml");
        //zip.unZipIt(INPUT_ZIP_FILE,OUTPUT_FOLDER);
    }

    public static void createZip(String dirpath) {
        byte[] buffer = new byte[1024];
        
        
        try {
            FileOutputStream fos = new FileOutputStream(dirpath + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            File directoryToZip = new File(dirpath);
            String[] sourceFiles = directoryToZip.list();
            for (int i = 0; i < sourceFiles.length; i++) {
                ZipEntry ze = new ZipEntry(sourceFiles[i]);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(dirpath + File.separator + sourceFiles[i]);

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            

            //remember close it
            zos.close();
            zos.flush();
            

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Unzip it
     *
     * @param zipFile input zip file
     * @param output zip file output folder
     */
    public void unZipIt(String zipFile, String outputFolder) {

        byte[] buffer = new byte[1024];

        try {

            //create output directory is not exists
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                deleteDirectory(folder);
                if (!folder.exists()) {
                    folder.mkdir();
                }
            }

            //get the zip file content
            ZipInputStream zis
                    = new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            

            File fileTemp = new File(zipFile);
            
            if (fileTemp.exists()) {
                fileTemp.delete();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        }

        // either file or an empty directory
        
        return dir.delete();
    }

}
