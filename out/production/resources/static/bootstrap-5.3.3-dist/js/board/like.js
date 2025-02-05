// HMTL 문서의 모든 내용이 로드 되었을 때 발생하는 이벤트 : DOMContentLoaded
document.addEventListener("DOMContentLoaded",function () {
    const likeIcon = document.querySelector("#LikeIcon")
    const likeCount = document.querySelector("#LikeCount")
    const userNo = document.querySelector("#userNo").value
    const boardId = document.querySelector("#boardId").value
    
    // 게시글 조회 시 좋아요 상태 확인
    try{
        // 비동기 실행
        fetch(`/posts/${boardId}/likes-status?userNo=${userNo}`)
            .then(response => response.json())
            .then(data =>{
                // 좋아요를 눌렸다면 좋아요 아이콘 활성화 
                if(data.isLiked){
                    likeIcon.textContent = "❤️"
                }
                // 좋아요를 누르지 않았다면 좋아요 아이콘 비활성화
                else{
                    likeIcon.textContent = "🤍"
                }
                // 좋아요 수 업데이트
                likeCount.textContent = data.likeCount
            })
            .catch(error => {
                alert("서버 오류 " + error.message)
            })
    }
    catch (error) {
        alert("예상치 못한 오류 발생 " + error.message)
    }

    // 좋아요 아이콘 클릭 이벤트
    likeIcon.addEventListener("click",function (e) {
        // 폼 제출 방지
        e.preventDefault()
        
        // 좋아요 토클
        try {
            // 비동기 실행
            fetch(`/posts/${boardId}/likes`,{
                method : "POST",
                headers : {
                    "Content-Type" : "application/json"
                },
                body : JSON.stringify({userNo : userNo, boardId :boardId})
            })
                .then(response => response.json())
                .then(data =>{
                    // 좋아요를 누른다면
                    if(data.isLiked){
                        likeIcon.textContent = "❤️"
                        likeCount.textContent = parseInt(likeCount.textContent) + 1;
                    }
                    // 좋아요를 취소한다면
                    else{
                        likeIcon.textContent = "🤍"
                        likeCount.textContent = parseInt(likeCount.textContent) - 1;
                    }
                })
                .catch(error => {
                    alert("좋아요 서버 오류 " + error.message)
                })

        }
        catch (error) {
            alert("좋아요 버튼 오류 발생 " + error.message)
        }

    })


})