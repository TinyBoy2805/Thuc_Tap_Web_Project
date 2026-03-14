const $ = document.querySelector.bind(document)
const $$ = document.querySelectorAll.bind(document)


$$('.tab-list__button').forEach(button => {
    button.addEventListener('click', function () {
        $$('.tab-list__button').forEach(btn => btn.classList.remove('tab-list__button--active'));
        $$('.auth-form').forEach(form => form.classList.remove('auth-form--active'));
        $$('.auth-form').forEach(form => form.classList.add('auth-form--hidden'));

        this.classList.add('tab-list__button--active');

        const targetFormId = this.getAttribute('data-form') + '-form';
        const targetForm = document.getElementById(targetFormId);
        if (targetForm) {
            targetForm.classList.add('auth-form--active');
            targetForm.classList.remove('auth-form--hidden');
        }
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const adminCheckbox = document.getElementById('adminCheckbox');
    const loginButton = document.getElementById('loginButton');

    const ADMIN_URL = './admin/pages/dashboard.html';
    const CUSTOMER_URL = '../../index.jsp';

    const updateLoginState = () => {
        const isChecked = adminCheckbox.checked;

        loginButton.href = isChecked ? ADMIN_URL : CUSTOMER_URL;
    };

    adminCheckbox.addEventListener('change', updateLoginState);

    updateLoginState();
});

//
// (function () {
//
//     const loginForm = document.getElementById('login-form');
//     if (!loginForm) return;
//
//     loginForm.addEventListener('submit', function (ev) {
//         ev.preventDefault();
//
//         const isAdmin = !!this.querySelector('#admin-checkbox')?.checked;
//
//         if (isAdmin) {
//             window.location.href = '../../../admin/pages/dashboard.jsp';
//
//         } else {
//
//             window.location.href = '../../../index.jsp';
//
//         }
//     });
// })();

