window.onload = function() {
    iniciar();
}

function iniciar() {
    botaoMenu();
}

function botaoMenu() {
    var botao = document.getElementById("omega-menu-button");

    botao.onclick = function() {
        if (document.getElementsByClassName("sidebar")[0].style.marginLeft == "-250px") {
            document.getElementsByClassName("topbar")[0].style.left = "250px"
            document.getElementsByClassName("sidebar")[0].style.marginLeft = "0"
            document.getElementsByClassName("main")[0].style.marginLeft = "250px";
            document.getElementsByClassName("footer")[0].style.marginLeft = "250px";
        } else {
            document.getElementsByClassName("topbar")[0].style.left = "0";
            document.getElementsByClassName("sidebar")[0].style.marginLeft = "-250px"
            document.getElementsByClassName("main")[0].style.marginLeft = "0";
            document.getElementsByClassName("footer")[0].style.marginLeft = "0";
        }
    }
}