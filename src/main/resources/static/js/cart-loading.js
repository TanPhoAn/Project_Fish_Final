function getCsrf() {
    const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const headerName = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    return { token, headerName };
}

//add cart
document.querySelectorAll(".add-to-cart-btn").forEach(btn => {

    btn.addEventListener("click", function(){

        const {token, headerName} = getCsrf();
        const productId = this.dataset.id;
        const headers = {
            "Content-Type": "application/x-www-form-urlencoded"
        };
        if (token && headerName) {
            headers[headerName] = token;
        }
        fetch("/cart/add", {
            method: "POST",
            headers: headers,
            body: "productId=" + encodeURIComponent(productId)
        })
            .then(res =>{
                if(!res.ok){
                    throw new Error("Load failed");
                }
                return res.text();
            })
            .then(data => {
                showToast("Item added successfully");
            })
            .catch(err => {
                showToast("Failed to add item ");
            })
        ;

    });

});

document.getElementById("cart-icon").addEventListener("click", ()=>{

    fetch("/cart/popup")
        .then(res => res.text())
        .then(html => {

            document.getElementById("cart-modal").classList.remove("hidden");
            document.getElementById("cart-modal-content").innerHTML = html;
        })
});

document.addEventListener("click", (e)=>{

        if(e.target.id === "cart-modal" || e.target.id === "close-cart"){
            document.getElementById("cart-modal").classList.add("hidden");
        }


});

document.addEventListener("click", function(e){
    if(e.target.closest(".remove-item-button")){
        const btn = e.target.closest(".remove-item-button");
        const itemId = btn.dataset.id;

        const { token, headerName } = getCsrf();

        const headers = {
            "Content-Type": "application/x-www-form-urlencoded"
        };
        if (token && headerName) headers[headerName] = token;

        fetch("cart/remove", {
            method: "POST",
            headers: headers,
            body: "itemId=" + encodeURIComponent(itemId)
        })
            .then(res => res.text())
            .then(()=>{
                alert("Remove item from cart");
                //reload
                reloadPopup();
            })
    }
});

function reloadPopup(){


    fetch("/cart/popup")
        .then(res => res.text())
        .then(html => {

            //document.getElementById("cart-modal").classList.remove("hidden");
            document.getElementById("cart-modal-content").innerHTML = html;
        })
}

function showToast(message){
    const container = document.getElementById("toast-container");
    const msg = document.getElementById("add-success-toast");

    msg.textContent = message;

    container.classList.remove("hidden");
    container.classList.add("flex");

    // Auto hide after 2.5s
    setTimeout(() => {
        container.classList.add("hidden");
    }, 2500);
}
// Close button
document.addEventListener("DOMContentLoaded", () => {
    const closeBtn = document.getElementById("toast-close");
    if (closeBtn) {
        closeBtn.addEventListener("click", () => {
            document.getElementById("toast-container")?.classList.add("hidden");
        });
    }
});
