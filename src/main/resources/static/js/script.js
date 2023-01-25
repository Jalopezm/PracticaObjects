document.getElementById('passwordConf').addEventListener('input',comparePass);
function comparePass() {
    let passConf = document.getElementById("passwordConf").value;
    let pass = document.getElementById("password").value;
        if(pass == passConf){
            document.getElementById("submit").disabled = false;
            document.getElementById("message").innerHTML = ""
        }else if(pass != passConf){
            document.getElementById("message").innerHTML = "Passwords Are Different"
            document.getElementById("submit").disabled = true;
        }
}


