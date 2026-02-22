// ─────────────────────────────────────────
// toast.js — Notification system
// ─────────────────────────────────────────

function toast(message, type = 'info', duration = 3500) {
  const icons = { success: '✓', error: '✕', info: '·' };

  const el = document.createElement('div');
  el.className = `toast ${type}`;
  el.innerHTML = `
    <span class="toast-icon">${icons[type]}</span>
    <span>${message}</span>
  `;

  document.getElementById('toasts').appendChild(el);
  setTimeout(() => el.remove(), duration);
}
