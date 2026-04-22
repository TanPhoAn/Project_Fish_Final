
    (() => {
    const searchInput = document.getElementById('product-search');
    const productGrid = document.getElementById('product-grid');
    const orderInput = document.getElementById("order-search");
    const orderTable = document.getElementById("order-table");
    const orderStatus = document.getElementById("order-status");
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
                        <a href="/admin/product/edit/${product.id}"
                           class="flex-1 text-center px-4 py-2 rounded-xl bg-blue-500 text-white hover:bg-blue-600 transition text-sm">
                            Edit
                        </a>

                        <a href="/admin/product/delete/${product.id}"
                           onclick="return confirm('Delete this product?')"
                           class="flex-1 text-center px-4 py-2 rounded-xl bg-red-500 text-white hover:bg-red-600 transition text-sm">
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
                case 'PAID':
                case 'COMPLETED':
                    return 'bg-green-100 text-green-700';
                default:
                    return 'bg-red-100 text-red-700';
            }
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
                <span class="px-3 py-1 rounded-full text-xs font-semibold ${orderStatusClass(order.status)}">
                    ${escapeHtml(order.status)}
                </span>
            </td>
            <td class="px-6 py-4 text-center">
                <a href="/order/details/${order.id}"
                   class="inline-block px-4 py-2 text-sm bg-blue-500 text-white rounded-xl hover:bg-blue-600 transition">
                    Detail
                </a>
            </td>
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
})();
