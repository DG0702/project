document.addEventListener("DOMContentLoaded",function (){
    
    // 모든 게시물 제목 
    const postLinks = document.querySelectorAll(".post-link")
    
    postLinks.forEach(links =>{
        links.addEventListener("click",function () {
            // 게시물 아이디 값 가져오기
            const boardId = this.getAttribute("data-board-id")

            // 비동기 실행
            fetch(`/posts/${boardId}/incrementViewCount`,{
                method : "POST",
                headers : {
                    "Content-Type" : "application/json"
                }
            })
                .then(response => {
                    if(!response.ok){
                        throw new Error(response.statusText)
                    }
                })
                .catch(error =>{
                    console.log(error)
                    alert("예상치 못한 오류 " + error.message)
                })

        })
    })
})