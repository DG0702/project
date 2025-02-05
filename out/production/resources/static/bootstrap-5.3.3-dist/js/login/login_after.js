// userNo 1로 로그인 되었을 때
// DOMContentLoaded : HTML 문서가 완전히 로드 되고 DOM 트리가 만들어 졌을 때 발생하는 이벤트
document.addEventListener("DOMContentLoaded",function (){

    let isAdmin = document.querySelector(".board").getAttribute("data-is-admin") === "true"

    let boardPost = document.querySelector("#board-post")
    let boardComment = document.querySelector("#board-comment")

    // 로그인 후 isAdmin true 일 경우
    if(isAdmin){
        // boardComment 존재할 경우
        if(boardComment){
            boardComment.style.display = "block"
            boardPost.style.width = "200px"
            boardPost.style.margin = "0px"
            boardPost.style.left = "30px"
            boardPost.style.top = "20px"
        }
    }
    

    // resize : 브라우저 창 크기가 바뀔 때 발생하는 이벤트 
    window.addEventListener("resize",function () {
        // resize 안에서 window.innerWidth 사용하면 동적으로 사용 -> 실시간 변경 
        // DOMContentLoaded 사용하면 페이지가 로드될 때 크기만 반영 ex) 페이지 로드 시 1000px 이였다면 계속 1000px
        let windowWidth = window.innerWidth
        if(windowWidth >= 768 && windowWidth <= 990){
            if (boardPost){
                boardPost.style.width = "100px"
                boardPost.style.height = "50px"
            }
            if (boardComment){
                boardComment.style.width = "100px"
                boardComment.style.height = "50px"
            }
        }else{
            if (boardPost){
                boardPost.style.width = "200px"
                boardPost.style.height = "100px"
            }
            if (boardComment){
                boardComment.style.width = "200px"
                boardComment.style.height = "100px"
            }
        }
    })
})