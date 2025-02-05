// 모든 삭제 버튼이 ->  삭제 버튼 눌렸을 경우 (각각 적용)
document.querySelectorAll("#post-delete").forEach(button =>{
    button.addEventListener("click",function (){
        // 클릭된 삭제 버튼이 속한 tr 요소 찾고 ->
        // 그 안에서 .post-link 클래스 값을 가진 요소 찾고 ->
        // 그 요소에 속성 data-board-id 에 속성값 가져오기
        const boardId = this.closest('tr').querySelector(".post-link").getAttribute("data-board-id")

        try{
            fetch("/manager/posts",{
                method : "DELETE",
                headers : {
                    "Content-Type" : "application/json"
                },
                body : JSON.stringify({boardId : boardId})
            })
                .then(response => response.json())
                .then(data => {
                    if(data.success){
                        alert("선택된 게시물이 삭제되었습니다.")
                        setTimeout(function() {
                            location.reload(); // 페이지 새로 고침
                        }, 100);
                    }else{
                        alert("게시물 삭제에 실패하였습니다.")
                    }
                })
                .catch(error =>{
                    alert("서버 오류로 인해 삭제 진행 불가 " + error.message)
                })
        }
        catch (error){
            alert("예상하지 못한 오류 발생 " + error.message)
        }


    })
})
