/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.view.pdfhelper;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

/**
 *
 * @author Manisha
 */
public class PdfPCellHelper extends PdfPCell{

    
    public PdfPCellHelper(Phrase phrase, int colspan, int border, int alignment) {
        super(phrase);
        super.setBorder(border);
        super.setColspan(colspan);
        super.setHorizontalAlignment(alignment);
        
    }

    
}
