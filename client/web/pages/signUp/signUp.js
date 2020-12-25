
function validateInput() {
    var username = $('#username')[0].value.trim();
    try{
        var userType = document.querySelector('input[name="userType"]:checked').value;
    }
    catch (e) {
        alert("ERROR. user type not selected")
        return;
    }
    if(username === ""){
        alert("ERROR. username is empty")
        return;
    }

    $.ajax({
        url: 'signUp',
        method:"POST",
        data: {"username": username, "userType": userType},
        success: function (respText,status,resp) {
            if(resp.getResponseHeader("error") !== null) {
                alert(resp.getResponseHeader("error"));
            }
            else if(resp.getResponseHeader("redirect") !== null){
                console.log(window.location.href)
                console.log(window.location)
                window.location.href = resp.getResponseHeader("redirect");
            }
        }
    })
}