// ─────────────────────────────────────────
// modal.js — Reusable modal system
// ─────────────────────────────────────────

function openModal(title, bodyHTML, footerHTML) {
  document.getElementById('modal-title').textContent  = title;
  document.getElementById('modal-body').innerHTML     = bodyHTML;
  document.getElementById('modal-footer').innerHTML   = footerHTML;
  document.getElementById('modal-overlay').classList.add('open');
}

function closeModal() {
  document.getElementById('modal-overlay').classList.remove('open');
}

// Close on backdrop click
document.getElementById('modal-overlay').addEventListener('click', (e) => {
  if (e.target === e.currentTarget) closeModal();
});
