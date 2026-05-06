const searchInput = document.getElementById('product-search');
const productGrid = document.getElementById('product-grid');
const orderInput = document.getElementById("order-search");
const orderTable = document.getElementById("order-table");
const orderStatus = document.getElementById("order-status");
const closeBtn = document.getElementById("close-user-btn");
const userModal = document.getElementById("user-edit-modal");
const editUserBtn = document.querySelectorAll(".user-edit-btn");
const userIdInput = document.getElementById("user-id");
const userNameInput = document.getElementById("user-name");
const userEmailInput = document.getElementById("user-email");
const userPhoneInput = document.getElementById("user-phone");
const userRoleInput = document.getElementById("user-role");
const userAddressInput = document.getElementById("user-address");
const cancelUserBtn = document.getElementById("cancel-user-modal");
const deleteUserBtnVer = document.getElementById("cancel-delete-user");
const deleteUserModal = document.getElementById("delete-user-modal");
const deleteUserBtn = document.querySelectorAll(".user-delete-btn");
const deleteUserForm = document.getElementById("delete-user-form");
const cancelCreateProd = document.getElementById("cancel-create-product");
const closeCreateProd = document.getElementById("close-create-product-btn");
const prodCreateModal = document.getElementById("product-create-modal");
const closeProdBtn = [closeCreateProd, cancelCreateProd];
const addProdBtn = document.getElementById("add-product-btn");
// product
const productDeleteModal = document.getElementById("product-delete-modal");
const productDeleteForm = document.getElementById("product-delete-form");

const cancelDeleteProd = document.getElementById("cancel-delete-product");

// edit product
const productEditModal = document.getElementById("product-edit-modal");
const productEditForm = document.getElementById("product-edit-form");
const closeEditProductBtn = document.getElementById("close-edit-product-btn");
const cancelEditProductBtn = document.getElementById("cancel-edit-product");

const editProductIdInput = document.getElementById("edit-product-id");
const editProductNameInput = document.getElementById("edit-product-name");
const editProductPriceInput = document.getElementById("edit-product-price");
const editProductQuantityInput = document.getElementById("edit-product-quantity");
const editProductDescriptionInput = document.getElementById("edit-product-description");

//csrf
const csrfToken =
    document.querySelector('meta[name="_csrf"]').content;

const csrfHeader =
    document.querySelector('meta[name="_csrf_header"]').content;


// pending
const pendingValue = document.getElementById("pending-order-value");
(() => {

    if (!searchInput || !productGrid || !orderInput || !orderTable) {
    return;
}

    let productDebounceTimer;
    let orderDebounceTimer;

    const escapeHtml = (value) => {
    const div = document.createElement('div');
    div.textContent = value ?? '';
    return div.innerHTML;
};

    const productCard = (product) => `
            <div class="bg-white rounded-2xl shadow hover:shadow-lg transition overflow-hidden border border-gray-100">
                <img src="${escapeHtml(product.imageUrl)}"
                     class="w-full h-52 object-cover"
                     alt="${escapeHtml(product.productName)}">

                <div class="p-5">
                    <span class="text-xs font-semibold px-3 py-1 rounded-full bg-blue-100 text-blue-600">
                        ${escapeHtml(product.categoryLabel)}
                    </span>

                    <h3 class="text-lg font-bold text-gray-800 mt-3">
                        ${escapeHtml(product.productName)}
                    </h3>

                    <p class="text-sm text-gray-500 mt-2 line-clamp-2">
                        ${escapeHtml(product.productDescription)}
                    </p>

                    <div class="mt-4 text-xl font-bold text-emerald-600">
                        ${product.productPrice} $
                    </div>

                    <p class="text-sm mt-1 text-gray-500">
                        Stock: <span>${product.quantity}</span>
                    </p>

                    <div class="mt-5 flex gap-2 flex-wrap">
                        <button data-id="${product.id}"
                           class="product-edit-btn flex-1 text-center px-4 py-2 rounded-xl bg-blue-500 text-white hover:bg-blue-600 transition text-sm">
                            Edit
                        </button>

                        <a data-id="${product.id}"
                           class="product-delete-btn flex-1 text-center px-4 py-2 rounded-xl bg-red-500 text-white hover:bg-red-600 transition text-sm">
                            Delete
                        </a>
                    </div>
                </div>
            </div>
        `;

    const orderStatusClass = (status) => {
        switch (status) {
                case 'PENDING':
                    return 'bg-yellow-100 text-yellow-700';
                case 'TRANSIT':
                    return 'bg-blue-100 text-blue-700';
                case 'COMPLETED':
                    return 'bg-green-100 text-green-700';
                default:
                    return 'bg-red-100 text-red-700';
            }
        };

    const orderStatusSelectClass = (status) => {
        switch (status) {
            case 'PENDING':
                return 'bg-yellow-100 text-yellow-700 border-yellow-200';
            case 'TRANSIT':
                return 'bg-blue-100 text-blue-700 border-blue-200';
            case 'COMPLETED':
                return 'bg-green-100 text-green-700 border-green-200';
            default:
                return 'bg-gray-100 text-gray-700 border-gray-200';
        }
    };

    const applyOrderStatusSelectClass = (select, status) => {
        if (!select) return;

        select.classList.remove(
            'bg-yellow-100', 'text-yellow-700', 'border-yellow-200',
            'bg-blue-100', 'text-blue-700', 'border-blue-200',
            'bg-green-100', 'text-green-700', 'border-green-200',
            'bg-gray-100', 'text-gray-700', 'border-gray-200'
        );

        orderStatusSelectClass(status).split(' ').forEach((className) => {
            if (className) {
                select.classList.add(className);
            }
        });
    };

    const updateOrderStatus = async (select) => {
        if (!select) {
            return;
        }

        const orderId = select.dataset.id;
        const status = select.value;
        const previousStatus = select.dataset.previousStatus || status;

        if (!orderId) {
            return;
        }

        select.disabled = true;

        try {
            const response = await fetch(`/admin/api/orders/${orderId}/status`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ status })
            });

            if (!response.ok) {
                throw new Error(`Order status update failed with status ${response.status}`);
            }

            const data = await response.json();
            select.value = data.status;
            select.dataset.previousStatus = data.status;
            applyOrderStatusSelectClass(select, data.status);
        } catch (error) {
            select.value = previousStatus;
            applyOrderStatusSelectClass(select, previousStatus);
            console.error('Failed to update order status', error);
        } finally {
            select.disabled = false;
        }
        pendingValue.textContent = await fetch(`/admin/api/orders/pending`).then(res=> res.json());

    };

    const emptyState = `
            <div class="col-span-full bg-white rounded-2xl border border-gray-100 p-8 text-center text-gray-500 shadow">
                No products found.
            </div>
        `;

    const orderEmptyDesktop = `
        <tr>
            <td colspan="6" class="px-6 py-8 text-center text-gray-500">
                No orders found.
            </td>
        </tr>
    `;
    // render
    const renderProducts = (items) => {
    if (!items.length) {
    productGrid.innerHTML = emptyState;
    return;
}

    productGrid.innerHTML = items.map(productCard).join('');
};

    const renderOrders = (items) => {
        if (orderTable) {
            orderTable.innerHTML = items.length
                ? items.map(orderRow).join('')
                : orderEmptyDesktop;
        }


        };
    // render
    //search
    const searchProducts = async () => {
    const params = new URLSearchParams({
    keyword: searchInput.value.trim()
});


    try {
    const response = await fetch(`/admin/api/products?${params.toString()}`, {
    headers: {
    'X-Requested-With': 'XMLHttpRequest'
}
});

    if (!response.ok) {
    throw new Error(`Search failed with status ${response.status}`);
}

    const data = await response.json();
    renderProducts(data.items || []);
} catch (error) {
    console.error('Failed to search products', error);
}
};

    const searchOrders = async () => {
            if (!orderInput) {
                return;
            }

            const params = new URLSearchParams({
                keyword: orderInput.value.trim()
            });

            if (orderStatus?.value) {
                params.set('status', orderStatus.value);
            }

            try {
                const response = await fetch(`/admin/api/orders?${params.toString()}`, {
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    }
                });

                if (!response.ok) {
                    throw new Error(`Order search failed with status ${response.status}`);
                }

                const data = await response.json();
                renderOrders(data.items || []);
            } catch (error) {
                console.error('Failed to search orders', error);
            }
        };
    // search

    const orderRow = (order) => `
        <tr class="border-t hover:bg-gray-50 transition">
            <td class="px-6 py-4 font-semibold text-gray-800">${order.id}</td>
            <td class="px-6 py-4">${escapeHtml(order.customerName)}</td>
            <td class="px-6 py-4 text-gray-500">${escapeHtml(order.orderDate)}</td>
            <td class="px-6 py-4 font-semibold text-blue-600">${order.totalPrice} $</td>
            <td class="px-6 py-4">
                <select data-id="${order.id}"
                        data-previous-status="${escapeHtml(order.status)}"
                        class="order-status-select rounded-xl px-3 py-2 text-xs font-semibold outline-none border ${orderStatusSelectClass(order.status)}">
                    <option value="PENDING" ${order.status === 'PENDING' ? 'selected' : ''}>PENDING</option>
                    <option value="TRANSIT" ${order.status === 'TRANSIT' ? 'selected' : ''}>TRANSIT</option>
                    <option value="COMPLETED" ${order.status === 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                </select>
            </td>
<!--        //     <td class="px-6 py-4 text-center">-->
<!--        //         <a href="/order/details/${order.id}"-->
<!--        //            class="inline-block px-4 py-2 text-sm bg-blue-500 text-white rounded-xl hover:bg-blue-600 transition">-->
<!--        //             Detail-->
<!--        //         </a>-->
<!--        //     </td>-->
         </tr>
    `;

    searchInput.addEventListener('input', () => {
    clearTimeout(productDebounceTimer);
    productDebounceTimer = setTimeout(searchProducts, 300);
});
        if (orderInput) {
            orderInput.addEventListener('input', () => {
                clearTimeout(orderDebounceTimer);
                orderDebounceTimer = setTimeout(searchOrders, 300);
            });
        }

        if (orderStatus) {
            orderStatus.addEventListener('change', searchOrders);
        }

        if (orderTable) {
            orderTable.addEventListener('change', async (e) => {
                const select = e.target.closest('.order-status-select');
                if (!select) {
                    return;
                }

                await updateOrderStatus(select);
            });
        }
})();

// close button for edit user
editUserBtn.forEach((btn)=>{
    btn.addEventListener("click", async ()=>{
        const userId = btn.dataset.id;
        if (!userId) {
            return;
        }
        userModal.classList.remove("hidden");
        try{
            const response = await fetch(`/admin/api/users/${userId}`, {
                headers: {
                    "X-Requested-With": "XMLHttpRequest"
                }
            });
            const user = await response.json();
            userNameInput.value = user.name;
            userEmailInput.value =  user.email;
            userPhoneInput.value = user.phone;
            userAddressInput.value = user.address;

            document.getElementById("user-edit-form").action = `/admin/user/${userId}/update`;

        }
        catch(error){

        }

    })
});
// close user
if(closeBtn){
    closeBtn.addEventListener("click", ()=>{
        userModal.classList.add("hidden");
    });
}
if(userModal){
    userModal.addEventListener("click", (e)=>{
        if(e.target === userModal || e.target.id === "user-edit-container"){
            userModal.classList.add("hidden");
        }
    })
}
if(cancelUserBtn){
    cancelUserBtn.addEventListener("click", ()=>{
        userModal.classList.add("hidden");
    });
}
if(deleteUserBtnVer){
    deleteUserBtnVer.addEventListener("click", ()=>{
        deleteUserModal.classList.add("hidden");
    });
}

closeProdBtn.forEach(btn => {
    if(btn){
        btn.addEventListener("click", ()=>{
            prodCreateModal?.classList.add("hidden");
        })
    }
})

if(prodCreateModal){
    prodCreateModal.addEventListener("click", (e)=>{
        const modalContent = prodCreateModal.querySelector('.bg-white');
        if (modalContent && !modalContent.contains(e.target)) {
            prodCreateModal.classList.add("hidden");
        }
    })
}

if(addProdBtn){
    addProdBtn.addEventListener("click", ()=>{
        prodCreateModal?.classList.remove("hidden");
    })
}

if(cancelDeleteProd){
    cancelDeleteProd.addEventListener("click", ()=>{
        productDeleteModal.classList.add("hidden");
    })
}

if(productDeleteModal){
    productDeleteModal.addEventListener("click", (e)=>{
        const modalContent = productDeleteModal.querySelector(".bg-white");
        if(productDeleteModal && !modalContent.contains(e.target)){
            productDeleteModal.classList.add("hidden");
        }
    })
}

productGrid.addEventListener("click", async (e) => {
    const editBtn = e.target.closest(".product-edit-btn");
    if (editBtn) {
        const productId = editBtn.dataset.id;

        if (!productId) return;

        productEditModal.classList.remove("hidden");

        try {
            const response = await fetch(`/admin/api/products/${productId}/get`, {
                headers: {
                    "X-Requested-With": "XMLHttpRequest"
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to load product: ${response.status}`);
            }

            const product = await response.json();

            editProductIdInput.value = product.id ?? "";
            editProductNameInput.value = product.productName ?? "";
            editProductPriceInput.value = product.productPrice ?? 0;
            editProductQuantityInput.value = product.quantity ?? 0;
            editProductDescriptionInput.value = product.productDescription ?? "";

            productEditForm.action = `/admin/product/${product.id}/update`;
        } catch (error) {
            console.error("Failed to load product", error);
        }

        return;
    }

    const deleteBtn = e.target.closest(".product-delete-btn");
    if (deleteBtn) {
        const productId = deleteBtn.dataset.id;
        if (!productId) return;

        productDeleteForm.action = `/admin/product/${productId}/remove`;
        productDeleteModal?.classList.remove("hidden");
    }
});

if (closeEditProductBtn) {
    closeEditProductBtn.addEventListener("click", () => {
        productEditModal.classList.add("hidden");
    });
}

if (cancelEditProductBtn) {
    cancelEditProductBtn.addEventListener("click", () => {
        productEditModal.classList.add("hidden");
    });
}

if (productEditModal) {
    productEditModal.addEventListener("click", (e) => {
        const modalContent = productEditModal.querySelector(".bg-white");
        if (modalContent && !modalContent.contains(e.target)) {
            productEditModal.classList.add("hidden");
        }
    });
}


deleteUserBtn.forEach(btn =>{
    btn.addEventListener("click", ()=>{
        const user_id = btn.dataset.id;
        deleteUserForm.action = `/admin/user/${user_id}/delete`;
        deleteUserModal.classList.remove("hidden");
    })
})
