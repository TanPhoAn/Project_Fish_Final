document.getElementById("user-detail-edit-button").addEventListener("click", ()=>{

    fetch("/user/profile/edit")
        .then(res => res.text())
        .then(html => {
            document.getElementById("user-info-modal").classList.remove("hidden");
            document.getElementById("user-info-modal-content").innerHTML = html;
        })
})


// function reloadUserProfilePopup(){
//
//
//     fetch("/user/profile")
//         .then(res => res.text())
//         .then(html => {
//
//             //document.getElementById("cart-modal").classList.remove("hidden");
//             document.getElementById("user-detail-hero").innerHTML = html;
//         })
// }