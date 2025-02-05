// 뉴스 수정 폼 제출 시
document.querySelector("#editNews").addEventListener("submit",function (e) {
    // 폼 제출 방지
    e.preventDefault()

    // 폼 데이터 가져오기
    const formData = new FormData(this)

    formData.append("menuId",1)


    // 첨부파일 모두 가져오기
    const files = formData.getAll("files")

    // 유효한 첨부파일만 가져오기
    const validFiles = files.filter(file => file.size > 0)

    // 첨부파일 유무
    if(validFiles.length > 0){
        formData.delete("files")
        validFiles.forEach(file => formData.append("files",file))
    }else{
        formData.delete("files")
    }

    try {
        fetch("/manager/news",{
            method : "PATCH",
            body : formData
        })
            .then(response =>{
                if(!response.ok){
                    throw new Error("오류 발생")
                }
                return response.json()
            })
            .then(data =>{
                if(data.success){
                    alert("뉴스 수정이 완료되었습니다")
                    // window.location.href = "" -> GET요청만 지원
                    window.location.href = "/after/news"
                }
                else{
                    alert(data.message)
                }
            })
            .catch(error =>{
                alert("뉴스 수정 시 오류 발생")
            })
    }
    catch (error){
        alert("예상치 못한 오류 " + error)
    }

})