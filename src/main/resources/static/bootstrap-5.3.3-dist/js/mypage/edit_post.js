// 게시물 수정 버튼 클릭 시 폼 제출
document.querySelector("#editForm").addEventListener("submit",function (e) {
    // 폼 제출 방지
    e.preventDefault()

    // 폼 데이터 가져오기
    const formData = new FormData(this);


    // 실제 게시물 ID 가져오기
    const boardId = document.querySelector("#boardId").value


    // 파일을 선택하지 않아도 빈 파일 객체로 보내기 때문에 -> 배열 안에 객체가 포함된 형태 -> 따라서 files 기본값은 1
    // 빈파일 형태 ->  [File { name: "", size: 0, type: "" }]

    // files 필드에서 빈 파일 객체 제거
    const files = formData.getAll("files")
    // 파일이 비어있지 않은 것만 필터링 -> 빈파일이 아닌 파일들만 필터링
    const validFiles = files.filter(file => file.size > 0)

    // 유효한 파일이 있다면
    if (validFiles.length > 0) {
        // 기존의 files 항목을 삭제
        formData.delete("files")

        // 유효한 파일만 다시 추가
        validFiles.forEach(file => formData.append("files", file))
    } else {
        // 유효한 파일이 없다면 files 필드를 삭제
        formData.delete("files")
    }



    try {
        fetch(`/posts/${boardId}`,{
            method : "PATCH",
            body : formData
        })
            .then(response => {
                if(!response.ok){
                    throw new Error("게시물 수정 응답 실패하였습니다")
                }
                return response.json()
            })
            .then(data => {
                window.location.href = `/users/posts`
            })
            .catch(error => {
                alert("게시물 수정에 오류 발생 " + error.message)
            })
    }
    catch (error) {
        alert("예상치 못한 오류 발생 " + error.message)
    }
})




function deleteAllFiles(){

    // 게시물 아이디
    const boardId = document.querySelector("#boardId").value

    try {
        fetch("/posts/files",{
            method : "DELETE",
            headers : {
                "Content-Type" : "application/json"
            },
            body : JSON.stringify({boardId : boardId})
        })
            .then(response => {
                if(!response.ok){
                    throw new Error("첨부파일 삭제 오류")
                }
                return response.json()
            })
            .then(data =>{
                if(data.success){
                    alert("첨부파일 모두 삭제되었습니다.")
                    // 삭제 성공 시 페이지 새로고침
                    location.reload();
                }else{
                    alert("첨부파일 삭제에 실패했습니다")
                }
            })
            .catch(error =>{
                alert("파일 삭제 오류 발생 " + error.message)
            })
    }
    catch(error){
        alert("예상 하지 못한 오류 발생 " + error.message)
    }
}