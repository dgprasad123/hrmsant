CKEDITOR.editorConfig = function( config ) {
	config.toolbar = [
        
		{ name: 'editing',     items: [ 'spellchecker' ] },
		
		'/',
		{ name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline' ] },
		{ name: 'paragraph', items: [ 'NumberedList' ] },
	];
	config.enterMode = CKEDITOR.ENTER_BR;
        //config.entities_processNumerical = 'force';
};