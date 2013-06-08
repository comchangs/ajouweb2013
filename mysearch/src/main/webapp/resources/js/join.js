function checkInput() {
	if (document.getElementById('userId').value != "" && document.getElementById('password').value != "") {
		return true;  
    } else {  
        return false;  
    }  
}

function checkPassword() {
	if (document.getElementById('password').value == document.getElementById('re_password').value) {  
        return true;  
    } else {  
        return false;  
    }  
}

function join_submit() {
	if(!checkInput()) {
		alert('Input all field!');
	} else if(!checkPassword()) {
		alert('Re-input password!');
	} else {
		document.getElementById("join").submit();
	}
}