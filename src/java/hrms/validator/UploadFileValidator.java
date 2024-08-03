/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.validator;

import hrms.model.upload.UploadedFile;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author Surendra
 */
public class UploadFileValidator implements Validator {

    

    public void validate(Object uploadedFile, Errors errors) {

        UploadedFile file = (UploadedFile) uploadedFile;

        if (file.getFile().getSize() == 0) {
            errors.rejectValue("file", "uploadForm.selectFile",
                    "Please select a file!");
        }
    }

    @Override
    public boolean supports(Class<?> type) {
         return false;  
    }
}
