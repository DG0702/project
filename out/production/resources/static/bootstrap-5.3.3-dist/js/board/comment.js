// 댓글 등록 폼 제출
document.querySelector("#comment-regi").addEventListener("submit",function (e) {
    // 폼 제출 방지
    e.preventDefault()


    const userNo = document.querySelector("#userNo").value
    const boardId = document.querySelector("#boardId").value
    const comment = document.querySelector("#comment").value
    const userNickname = document.querySelector("#writer").innerText
    const replyCount = document.querySelector("#replyCount")


    try{
        // 비동기 실행
        fetch(`/comments/new`,{
            method : "POST",
            headers : {
                "Content-Type" : "application/json"
            },
            body : JSON.stringify({
                userNo: userNo, boardId : boardId, userNickname : userNickname , comment : comment
            })
        })
            .then(response => {
                if(!response.ok){
                    alert("서버 오류 발생")
                }else{
                    return response.json()
                }
            })
            .then(data =>{
                if(data.success){
                    const commentList = document.querySelector("#comment-list")
                    const newCommentItem = document.createElement("li")

                    newCommentItem.classList.add("comment-item")

                    newCommentItem.innerHTML =
                        `<div class="comment-area">
                                <div id="show-writer" class="comment-writer">
                                    ${data.userNickname}
                                </div>
                                <div id="show-comment" class="comment-content">
                                    ${data.comment}
                                </div>
                                <div id="show-time" class="comment-footer">
                                    ${data.formattedCreateTime}
                                </div>
                            </div>`

                    commentList.prepend(newCommentItem)
                    document.querySelector("#comment").value = "";

                    // 댓글 수 증가 코드
                    if(replyCount){
                        replyCount.textContent = parseInt(replyCount.textContent) + 1
                    }

                }
            })
            .catch(error =>{
                alert("댓글 오류 발생" + error.message)
            })
    }
    catch (error) {
        alert("예상치 못한 오류 발생" + error.message)
    }
})