

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

                showToast("success","Item added successfully");
            })
            .catch(err => {
                showToast("success","Failed to add item ");
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

        if(e.target.id === "cart-modal" || e.target.id === "user-info-modal" || e.target.id === "close-cart" ){
            document.getElementById("cart-modal").classList.add("hidden");
            document.getElementById("user-info-modal").classList.add("hidden");
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
            .then(res =>{
                if(!res.ok){
                    throw new Error("load failed");
                }
                return res.text();
            })
            .then(data=>{


                showToast("delete","Remove item from cart");

                //reload
                reloadPopup();
            })
            .catch(err => {
                showToast("delete","Fail to remove item");
            });
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

function getCsrf() {
    const token = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const headerName = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    return { token, headerName };
}


function showToast(type, message) {
    const container = document.getElementById("toast-container");
    const template = document.getElementById("toast-template");

    if (!container || !template) return;

    container.classList.remove("hidden");

    // Clone template
    const toast = template.cloneNode(true);
    toast.id = "";
    toast.classList.remove("hidden");
    toast.classList.add("toast-item");

    // Inject message
    toast.querySelector("#toast-message").textContent = message;

    // Inject icon
    const iconInject = toast.querySelector("#icon-inject");

    const iconMap = {
        success: `<svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 11.917 9.724 16.5 19 7.5"/></svg>`,
        delete: `<svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18 17.94 6M18 18 6.06 6"/></svg>`,
        warning: `<svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 13V8m0 8h.01M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"/></svg>`,
        info: `<svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" stroke-width="2" d="M12 8h.01M12 12v4m0-10a9 9 0 110 18 9 9 0 010-18z"/></svg>`
    };

    iconInject.innerHTML = iconMap[type];

    // Apply color style
    const styleMap = {
        success: "bg-green-50 border-green-300",
        delete: "bg-red-50 border-red-300",
        warning: "bg-yellow-50 border-yellow-300",
        info: "bg-blue-50 border-blue-300",
    };
    //split ra 1 mảng, ... bung mảng ra 2 element -> add
    toast.classList.add(...styleMap[type].split(" "));

    // Close button
    const closeBtn = toast.querySelector(".toast-close");
    closeBtn.onclick = () => toast.remove();

    // Auto remove
    setTimeout(() => {
        toast.classList.add("opacity-0");
        setTimeout(() => toast.remove(), 300);
    }, 3000);

    // Add to container (queue)
    container.appendChild(toast);
}





