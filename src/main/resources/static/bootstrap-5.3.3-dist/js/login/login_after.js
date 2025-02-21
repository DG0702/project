// userNo 1로 로그인 되었을 때
// DOMContentLoaded : HTML 문서가 완전히 로드 되고 DOM 트리가 만들어 졌을 때 발생하는 이벤트
document.addEventListener("DOMContentLoaded", function () {

    const admin = document.querySelector("#admin")
    const user = document.querySelector("#user")
    const isAdmin = admin.getAttribute("data-is-admin") === "true"

    if(isAdmin){
        admin.style.display = "block"
        user.style.display = "none"
    }else{
        admin.style.display = "none"
        user.style.display = "block"
    }
})