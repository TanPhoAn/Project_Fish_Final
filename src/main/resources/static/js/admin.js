
    (() => {
    const searchInput = document.getElementById('product-search');
    const productGrid = document.getElementById('product-grid');

    if (!searchInput || !productGrid) {
    return;
}

    let debounceTimer;

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

    const emptyState = `
            <div class="col-span-full bg-white rounded-2xl border border-gray-100 p-8 text-center text-gray-500 shadow">
                No products found.
            </div>
        `;

    const renderProducts = (items) => {
    if (!items.length) {
    productGrid.innerHTML = emptyState;
    return;
}

    productGrid.innerHTML = items.map(productCard).join('');
};

    const searchProducts = async () => {
    const params = new URLSearchParams({
    keyword: searchInput.value.trim(),
    size: '12'
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

    searchInput.addEventListener('input', () => {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(searchProducts, 300);
});
})();
