
    const avatarInput = document.getElementById("avatarInput");
    const avatarPreview = document.getElementById("avatarPreview");
    const avatarSubmitBtn = document.getElementById("saveAvatarBtn");
    let selectedFile = null;
    avatarInput.addEventListener("change", function () {
    const file = this.files[0];

    if(!file) return;
    selectedFile = file;


    const reader = new FileReader();

    reader.onload = function (e) {
        avatarPreview.src = e.target.result;
    }

    reader.readAsDataURL(file);

    avatarSubmitBtn.classList.remove("hidden");

    });

    // avatarSubmitBtn.addEventListener("click", function(){
    //     avatarPreview.src = selectedFile;
    //
    // })



