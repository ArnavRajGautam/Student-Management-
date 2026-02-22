// ─────────────────────────────────────────
// students.js — Students page CRUD
// Endpoints used:
//   GET    /api/students
//   GET    /api/students/:id
//   POST   /api/students
//   PUT    /api/students/:id
//   DELETE /api/students/:id
// ─────────────────────────────────────────

let allStudents = [];

// ── RENDER PAGE ──
async function students() {
  document.getElementById('main-content').innerHTML = `
    <div class="page-header">
      <div>
        <div class="page-title">Students</div>
        <div class="page-subtitle">Manage all student records</div>
      </div>
      <button class="btn btn-primary" onclick="openAddStudent()">+ Add Student</button>
    </div>
    <div class="section">
      <div class="toolbar">
        <div class="search-box">
          <span class="search-icon">⌕</span>
          <input type="text" placeholder="Search by name or email..." oninput="filterStudents(this.value)" />
        </div>
        <button class="btn btn-ghost" onclick="loadStudents()">↻ Refresh</button>
      </div>
      <div id="student-table">
        <div class="loading"><div class="spinner"></div>Loading students...</div>
      </div>
    </div>`;

  await loadStudents();
}

// ── LOAD ALL ──
async function loadStudents() {
  try {
    const res = await api.get('/students');
    allStudents = res.data || [];
    document.getElementById('student-count').textContent = allStudents.length;
    renderStudentTable(allStudents);
  } catch (e) {
    document.getElementById('student-table').innerHTML =
      `<div class="empty"><div class="empty-icon">✕</div><div class="empty-text">${e.message}</div></div>`;
    toast(e.message, 'error');
  }
}

// ── FILTER ──
function filterStudents(query) {
  const q = query.toLowerCase();
  const filtered = allStudents.filter(s =>
    `${s.firstName} ${s.lastName}`.toLowerCase().includes(q) ||
    s.email.toLowerCase().includes(q)
  );
  renderStudentTable(filtered);
}

// ── RENDER TABLE ──
function renderStudentTable(data) {
  const el = document.getElementById('student-table');
  if (!data.length) {
    el.innerHTML = `<div class="empty"><div class="empty-icon">◎</div><div class="empty-text">No students found</div></div>`;
    return;
  }
  el.innerHTML = `
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Student</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Date of Birth</th>
            <th>Enrolled On</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          ${data.map(s => `
            <tr>
              <td>
                <div class="cell-student">
                  <div class="avatar">${initials(s.firstName, s.lastName)}</div>
                  <div>
                    <div class="cell-name">${s.firstName} ${s.lastName}</div>
                    <div class="cell-sub">#${s.id}</div>
                  </div>
                </div>
              </td>
              <td style="font-family:'DM Mono',monospace;font-size:12px;color:var(--muted)">${s.email}</td>
              <td style="color:var(--muted)">${s.phone || '—'}</td>
              <td style="color:var(--muted);font-family:'DM Mono',monospace;font-size:12px">${s.dateOfBirth || '—'}</td>
              <td style="color:var(--muted);font-family:'DM Mono',monospace;font-size:12px">${s.enrollmentDate || '—'}</td>
              <td>
                <div class="actions">
                  <button class="btn btn-ghost btn-sm" onclick="openEditStudent(${s.id})">Edit</button>
                  <button class="btn btn-danger btn-sm" onclick="confirmDeleteStudent(${s.id}, '${s.firstName} ${s.lastName}')">Delete</button>
                </div>
              </td>
            </tr>`).join('')}
        </tbody>
      </table>
    </div>`;
}

// ── FORM HTML ──
function studentFormHTML(s = {}) {
  return `
    <div class="form-grid">
      <div class="form-group">
        <label>First Name *</label>
        <input id="f-firstName" value="${s.firstName || ''}" placeholder="John" />
      </div>
      <div class="form-group">
        <label>Last Name *</label>
        <input id="f-lastName" value="${s.lastName || ''}" placeholder="Doe" />
      </div>
      <div class="form-group full">
        <label>Email *</label>
        <input id="f-email" type="email" value="${s.email || ''}" placeholder="john@example.com" />
      </div>
      <div class="form-group">
        <label>Phone *</label>
        <input id="f-phone" value="${s.phone || ''}" placeholder="9876543210" maxlength="10" />
      </div>
      <div class="form-group">
        <label>Date of Birth</label>
        <input id="f-dob" type="date" value="${s.dateOfBirth || ''}" />
      </div>
      <div class="form-group full">
        <label>Address</label>
        <textarea id="f-address" placeholder="Street, City, State">${s.address || ''}</textarea>
      </div>
    </div>`;
}

// ── COLLECT FORM DATA ──
function getStudentFormData() {
  return {
    firstName:   document.getElementById('f-firstName').value.trim(),
    lastName:    document.getElementById('f-lastName').value.trim(),
    email:       document.getElementById('f-email').value.trim(),
    phone:       document.getElementById('f-phone').value.trim(),
    dateOfBirth: document.getElementById('f-dob').value || null,
    address:     document.getElementById('f-address').value.trim() || null,
  };
}

// ── ADD ──
function openAddStudent() {
  openModal('Add Student', studentFormHTML(), `
    <button class="btn btn-ghost" onclick="closeModal()">Cancel</button>
    <button class="btn btn-primary" onclick="submitAddStudent()">Create Student</button>`);
}

async function submitAddStudent() {
  const data = getStudentFormData();
  if (!data.firstName || !data.lastName || !data.email || !data.phone) {
    toast('Please fill all required fields', 'error'); return;
  }
  try {
    await api.post('/students', data);
    toast('Student created successfully!', 'success');
    closeModal();
    await loadStudents();
  } catch (e) { toast(e.message, 'error'); }
}

// ── EDIT ──
async function openEditStudent(id) {
  try {
    const res = await api.get(`/students/${id}`);
    openModal('Edit Student', studentFormHTML(res.data), `
      <button class="btn btn-ghost" onclick="closeModal()">Cancel</button>
      <button class="btn btn-primary" onclick="submitEditStudent(${id})">Save Changes</button>`);
  } catch (e) { toast(e.message, 'error'); }
}

async function submitEditStudent(id) {
  try {
    await api.put(`/students/${id}`, getStudentFormData());
    toast('Student updated!', 'success');
    closeModal();
    await loadStudents();
  } catch (e) { toast(e.message, 'error'); }
}

// ── DELETE ──
function confirmDeleteStudent(id, name) {
  openModal('Delete Student',
    `<p style="color:var(--muted);font-size:14px">
      Are you sure you want to delete <strong style="color:var(--text)">${name}</strong>?
      This action cannot be undone.
    </p>`, `
    <button class="btn btn-ghost" onclick="closeModal()">Cancel</button>
    <button class="btn btn-danger" onclick="submitDeleteStudent(${id})">Delete</button>`);
}

async function submitDeleteStudent(id) {
  try {
    await api.delete(`/students/${id}`);
    toast('Student deleted', 'success');
    closeModal();
    await loadStudents();
  } catch (e) { toast(e.message, 'error'); }
}

// ── UTIL ──
function initials(first, last) {
  return ((first?.[0] || '') + (last?.[0] || '')).toUpperCase();
}
