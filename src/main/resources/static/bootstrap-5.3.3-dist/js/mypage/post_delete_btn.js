// 삭제 버튼 클릭 시
document.querySelector("#post-delete-btn").addEventListener("click",function (){

    // 선택된 게시물 체크박스 값 가져오기
    const selectCheckBoxes = document.querySelectorAll('input[name="check-num"]:checked')


    if(selectCheckBoxes.length ===0){
        alert("삭제할 게시물을 선택해주세요.")
        return;
    }

    // 배열로 이동
    const selectData= [];
    selectCheckBoxes.forEach(function (checkbox){
        const boardId = checkbox.value;
        selectData.push(boardId)
    })

    try{
        fetch("/users/posts",{
            method : "DELETE",
            headers : {
                "Content-Type" : "application/json"
            },
            body : JSON.stringify({boardIds : selectData})
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