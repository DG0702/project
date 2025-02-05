document.querySelectorAll("#comment-delete").forEach(button =>{
    button.addEventListener("click",function () {
        // 댓글 아이디 가져오기
        const commentId = this.getAttribute("data-comment-id")

        try{
            fetch("/manager/comment",{
                method : "DELETE",
                headers : {
                    "Content-Type" : "application/json"
                },
                body : JSON.stringify({commentId : commentId})
            })
                .then(response => {
                    if (!response.ok){
                        throw new Error("응답 오류 발생")
                    }
                    return response.json()
                })
                .then(data =>{
                    if(data.success){
                        alert("댓글을 삭제 하였습니다.")
                        setTimeout(function () {
                            location.reload()
                        },100)
                    }else{
                        alert("댓글 삭제를 실패했습니다")
                    }
                })
                .catch(error =>{
                    alert("댓글 삭제 오류 발생 " + error.message)
                })
        }
        catch (error) {
            alert("예상치 못한 오류 발생 " + error.message)
        }
    })
})