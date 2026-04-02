// select payment method
const codBtn = document.getElementById("codBtn");
const bankBtn = document.getElementById("bankBtn");
const submitPaymentMethod = document.getElementById("submitPaymentMethod");
const submitButton = document.getElementById("order-submit-button");
const emptyItemMsg = document.getElementById("empty-item-alert-msg");
const cartItem = document.querySelectorAll(".cart-list-item");
function resetButtons() {
    document.querySelectorAll(".payment-option")
        .forEach(btn => btn.classList.remove("ring-2", "ring-blue-500", "bg-blue-100"));
}

codBtn.addEventListener("click", () => {
    resetButtons();
    codBtn.classList.add("ring-2", "ring-blue-500", "bg-blue-100");
    submitPaymentMethod.value = "COD";
    document.getElementById("paymentMethod-warningText").classList.add("hidden");
});

bankBtn.addEventListener("click", () => {
    resetButtons();
    bankBtn.classList.add("ring-2", "ring-blue-500", "bg-blue-100");
    submitPaymentMethod.value = "BANK";
    document.getElementById("paymentMethod-warningText").classList.add("hidden");
});


// reloading animation
submitButton.addEventListener("click", function(e){
    if(submitPaymentMethod.value.trim() === ""){
        e.preventDefault();

        document.getElementById("paymentMethod-warningText").classList.remove("hidden");
    }
    else{
        document.getElementById("paymentMethod-warningText").classList.add("hidden");
    }
    if(cartItem.length === 0){
        submitButton.disabled = true;
        emptyItemMsg.classList.remove("hidden");
    }
    else{
        emptyItemMsg.classList.add("hidden");
    }
});



