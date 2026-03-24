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
            .then(res => res.text())
            .then(data => {
                alert("Added to cart");
            });

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