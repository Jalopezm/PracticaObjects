document.getElementById('password').addEventListener('change',comparePass);
document.getElementById('passwordConf').addEventListener('change',comparePass);
function comparePass() {
    let passConf = document.getElementById("passwordConf").value;
    let pass = document.getElementById("password").value;
        if(pass == passConf){
            document.getElementById("submit").disabled = false;
            alert("Pass E");
        }else if(pass != passConf){
            alert("Passwords are Different");
            document.getElementById("submit").disabled = true;
        }
}


