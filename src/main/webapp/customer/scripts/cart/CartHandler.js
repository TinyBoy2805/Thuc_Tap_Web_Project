


document.addEventListener("DOMContentLoaded", () =>
{
    const checkAll = document.querySelector('#check-all');
    const payBtn = document.querySelector(".pay-btn");
    const checkedItems = [];

    const getAllCheckedItems = () =>
    {
        checkedItems.length = 0;
        document.querySelectorAll('.cart__select-input:checked').forEach(item =>
        {
            checkedItems.push(item.id);
        });
    };

    checkAll.addEventListener('change', () =>
    {
        // query lại mỗi lần để đảm bảo NodeList luôn chính xác
        document.querySelectorAll('.cart__select-input').forEach(cb =>
        {
            cb.checked = checkAll.checked;
        });
    });

    // dùng event delegation cho tất cả checkbox
    document.querySelector('.cart__table-body').addEventListener('change', e =>
    {
        const target = e.target;
        if (target.classList.contains('cart__select-input'))
        {
            const total = document.querySelectorAll('.cart__select-input').length;
            const checked = document.querySelectorAll('.cart__select-input:checked').length;
            checkAll.checked = total === checked;
            getAllCheckedItems();
        }
    });

    // gán admin.listener chung cho tbody
    document.querySelector('.cart__table-body').addEventListener('click', async (e) =>
    {
        const delBtn = e.target.closest('.cart__remove-btn');
        if (!delBtn) return;

        const row = delBtn.closest('.cart__row');
        if (!row) return;

        const itemId = row.id;


        await fetch(`${window.APP_CONTEXT_PATH}/cart?cartItemId=${itemId}`,
        {
            method: "DELETE"
        })
        .then(res => res.json())
        .then(data =>
        {
            console.log("Deleted:", data);
            location.reload();
        })
        .catch(err => console.error(err));
    });


    payBtn.addEventListener("click", () =>
    {
        getAllCheckedItems();
        console.log("checkedItems:", checkedItems);
    });
});
