function fn_submitChk () {
    if (regform.pw.value != regform.confirm_pwd.value) {
        regform.pw.focus();
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }
    if (regform.name.value == "") {
        regform.name.focus();
        alert("이름을 입력해주세요.");
        return false;
    }
    if (regform.phone.value == "") {
        regform.phone.focus();
        alert("휴대폰 번호를 입력해주세요");
        return false;
    }
    if (regform.useremail.value == "") {
        regform.useremail.focus();
        alert("이메일을 입력해주세요.");
        return false;
    }
    if (regform.emailChk.value == "N") {
        regform.emailChk.focus();
        alert("아이디 중복확인이 필요합니다.");
        return false;
    }
};

function fn_emailChk() {
    // request 파라미터에 userid 를 넣어서 서버단으로 보냄
    var params = {
        username: $('#useremail').val()
    }

    $.ajax({
        url: "/user/emailChk", //데이터 주소
        type: "POST", //보내는방식 get 혹은 post
        dataType: 'json', //서버로부터 받는 값의 데이터 형식
        data: params, //보내는 데이터 형식

        // 서버에서 데이터가 넘어오면 success 에 들어옴 이때 result는 서버에서 응답받아서 넘어온 데이터

        success: function (result) {

            // json 방식으로 넘어온 데이터는 key : value 형식이기 때문에 result.result 로 해야 값을 가져올 수 있음
            var emailChk = result.result
            if (emailChk == false) {
                $('#emailChk').attr("value", "N");
                alert("중복된 아이디예염! 다른걸로 ㄱㄱ");

            } else if (emailChk == true) {
                $('#emailChk').attr("value", "Y");
                alert("굿 잘지었음 ㄱㄱ")

            } else if (emailChk == "") {
                alert("아이디는입력하셔야졍;;");
            }
        }
    });
}