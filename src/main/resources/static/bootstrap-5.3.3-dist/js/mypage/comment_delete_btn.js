// 삭제 버튼을 눌렸을 경우
document.querySelector("#comment-delete-btn").addEventListener("click",function () {

    // 체크리스트
    const selectCheckBoxes = document.querySelectorAll('input[name="check-num"]:checked')

    if(selectCheckBoxes.length === 0){
        alert("삭제할 댓글을 선택해주세요")
        return
    }
    
    // 다수의 데이터를 가지고 와야하기 때문에 배열 선택
    const selectData = [];
    selectCheckBoxes.forEach(function (checkbox) {
        const commentId = checkbox.value
        selectData.push(commentId)
    })

    try{
        fetch("/users/comment",{
            method : "DELETE",
            headers : {
                "Content-Type" : "application/json"
            },
            body : JSON.stringify({commentIds : selectData})
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