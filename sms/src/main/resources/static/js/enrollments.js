// ─────────────────────────────────────────
// enrollments.js — Enrollments page
// Endpoints used:
//   GET    /api/enrollments
//   POST   /api/enrollments
//   PATCH  /api/enrollments/:id/grade?grade=A+
//   PATCH  /api/enrollments/:id/status?status=ACTIVE
//   DELETE /api/enrollments/:id
// ─────────────────────────────────────────

let allEnrollments = [];
let statusFilter   = '';

// ── RENDER PAGE ──
async function enrollments() {
  document.getElementById('main-content').innerHTML = `
    <div class="page-header">
      <div>
        <div class="page-title">Enrollments</div>
        <div class="page-subtitle">Track student-course enrollments</div>
      </div>
      <button class="btn btn-primary" onclick="openAddEnrollment()">+ Enroll Student</button>
    </div>
    <div class="section">
      <div class="toolbar">
        <div class="search-box">
          <span class="search-icon">⌕</span>
          <input type="text" placeholder="Search by student or course..." oninput="filterEnrollments(this.value)" />
        </div>
        <select onchange="setStatusFilter(this.value)" style="width:auto">
          <option value="">All Status</option>
          <option value="ACTIVE">Active</option>
          <option value="COMPLETED">Completed</option>
          <option value="DROPPED">Dropped</option>
        </select>
        <button class="btn btn-ghost" onclick="loadEnrollments()">↻ Refresh</button>
      </div>
      <div id="enrollment-table">
        <div class="loading"><div class="spinner"></div>Loading enrollments...</div>
      </div>
    </div>`;

  await loadEnrollments();
}

// ── LOAD ALL ──
async function loadEnrollments() {
  try {
    const res = await api.get('/enrollments');
    allEnrollments = res.data || [];
    document.getElementById('enrollment-count').textContent = allEnrollments.length;
    applyEnrollmentFilters();
  } catch (e) {
    document.getElementById('enrollment-table').innerHTML =
      `<div class="empty"><div class="empty-icon">✕</div><div class="empty-text">${e.message}</div></div>`;
    toast(e.message, 'error');
  }
}

// ── FILTER ──
function filterEnrollments(query) {
  const q = query.toLowerCase();
  const filtered = allEnrollments.filter(e =>
    (e.studentName || '').toLowerCase().includes(q) ||
    (e.courseName  || '').toLowerCase().includes(q)
  );
  renderEnrollmentTable(statusFilter ? filtered.filter(e => e.status === statusFilter) : filtered);
}

function setStatusFilter(value) {
  statusFilter = value;
  applyEnrollmentFilters();
}

function applyEnrollmentFilters() {
  const filtered = statusFilter
    ? allEnrollments.filter(e => e.status === statusFilter)
    : allEnrollments;
  renderEnrollmentTable(filtered);
}

// ── RENDER TABLE ──
function renderEnrollmentTable(data) {
  const el = document.getElementById('enrollment-table');
  if (!data.length) {
    el.innerHTML = `<div class="empty"><div class="empty-icon">⟐</div><div class="empty-text">No enrollments found</div></div>`;
    return;
  }
  el.innerHTML = `
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Student</th>
            <th>Course</th>
            <th>Status</th>
            <th>Grade</th>
            <th>Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          ${data.map(e => `
            <tr>
              <td style="font-family:'DM Mono',monospace;font-size:11px;color:var(--muted)">${e.id}</td>
              <td style="font-weight:500">${e.studentName || `Student #${e.studentId}`}</td>
              <td style="color:var(--muted)">${e.courseName || `Course #${e.courseId}`}</td>
              <td>${statusBadge(e.status)}</td>
              <td>
                ${e.grade
                  ? `<span style="font-family:'DM Mono',monospace;color:var(--accent2);font-weight:500">${e.grade}</span>`
                  : `<span style="color:var(--muted)">—</span>`}
              </td>
              <td style="color:var(--muted);font-family:'DM Mono',monospace;font-size:11px">
                ${e.enrollmentDate || '—'}
              </td>
              <td>
                <div class="actions">
                  <button class="btn btn-ghost btn-sm" onclick="openUpdateGrade(${e.id}, '${e.grade || ''}')">Grade</button>
                  <button class="btn btn-ghost btn-sm" onclick="openUpdateStatus(${e.id}, '${e.status}')">Status</button>
                  <button class="btn btn-danger btn-sm" onclick="confirmDeleteEnrollment(${e.id})">Delete</button>
                </div>
              </td>
            </tr>`).join('')}
        </tbody>
      </table>
    </div>`;
}

// ── ADD ENROLLMENT ──
async function openAddEnrollment() {
  let studentOpts = '<option value="">Select a student</option>';
  let courseOpts  = '<option value="">Select a course</option>';
  try {
    const [sRes, cRes] = await Promise.all([api.get('/students'), api.get('/courses')]);
    (sRes.data || []).forEach(s => {
      studentOpts += `<option value="${s.id}">${s.firstName} ${s.lastName}</option>`;
    });
    (cRes.data || []).forEach(c => {
      courseOpts += `<option value="${c.id}">${c.courseName} (${c.courseCode})</option>`;
    });
  } catch (e) {
    toast('Could not load students or courses', 'error'); return;
  }

  openModal('Enroll Student', `
    <div class="form-grid">
      <div class="form-group full">
        <label>Student *</label>
        <select id="e-student">${studentOpts}</select>
      </div>
      <div class="form-group full">
        <label>Course *</label>
        <select id="e-course">${courseOpts}</select>
      </div>
    </div>`, `
    <button class="btn btn-ghost" onclick="closeModal()">Cancel</button>
    <button class="btn btn-primary" onclick="submitAddEnrollment()">Enroll</button>`);
}

async function submitAddEnrollment() {
  const studentId = document.getElementById('e-student').value;
  const courseId  = document.getElementById('e-course').value;
  if (!studentId || !courseId) {
    toast('Please select both a student and a course', 'error'); return;
  }
  try {
    await api.post('/enrollments', { studentId: +studentId, courseId: +courseId });
    toast('Student enrolled successfully!', 'success');
    closeModal();
    await loadEnrollments();
  } catch (e) { toast(e.message, 'error'); }
}

// ── UPDATE GRADE ──
function openUpdateGrade(id, currentGrade) {
  const grades = ['A+', 'A', 'B+', 'B', 'C+', 'C', 'D', 'F'];
  openModal('Update Grade', `
    <div class="form-group">
      <label>Select Grade</label>
      <select id="g-grade">
        ${grades.map(g => `<option value="${g}" ${currentGrade === g ? 'selected' : ''}>${g}</option>`).join('')}
      </select>
    </div>`, `
    <button class="btn btn-ghost" onclick="closeModal()">Cancel</button>
    <button class="btn btn-primary" onclick="submitUpdateGrade(${id})">Update Grade</button>`);
}

async function submitUpdateGrade(id) {
  const grade = document.getElementById('g-grade').value;
  try {
    await api.patch(`/enrollments/${id}/grade?grade=${grade}`);
    toast('Grade updated!', 'success');
    closeModal();
    await loadEnrollments();
  } catch (e) { toast(e.message, 'error'); }
}

// ── UPDATE STATUS ──
function openUpdateStatus(id, currentStatus) {
  openModal('Update Status', `
    <div class="form-group">
      <label>Select Status</label>
      <select id="s-status">
        ${['ACTIVE', 'COMPLETED', 'DROPPED'].map(s =>
          `<option value="${s}" ${currentStatus === s ? 'selected' : ''}>${s}</option>`
        ).join('')}
      </select>
    </div>`, `
    <button class="btn btn-ghost" onclick="closeModal()">Cancel</button>
    <button class="btn btn-primary" onclick="submitUpdateStatus(${id})">Update Status</button>`);
}

async function submitUpdateStatus(id) {
  const status = document.getElementById('s-status').value;
  try {
    await api.patch(`/enrollments/${id}/status?status=${status}`);
    toast('Status updated!', 'success');
    closeModal();
    await loadEnrollments();
  } catch (e) { toast(e.message, 'error'); }
}

// ── DELETE ──
function confirmDeleteEnrollment(id) {
  openModal('Delete Enrollment',
    `<p style="color:var(--muted);font-size:14px">
      Delete enrollment <strong style="color:var(--text)">#${id}</strong>? This cannot be undone.
    </p>`, `
    <button class="btn btn-ghost" onclick="closeModal()">Cancel</button>
    <button class="btn btn-danger" onclick="submitDeleteEnrollment(${id})">Delete</button>`);
}

async function submitDeleteEnrollment(id) {
  try {
    await api.delete(`/enrollments/${id}`);
    toast('Enrollment deleted', 'success');
    closeModal();
    await loadEnrollments();
  } catch (e) { toast(e.message, 'error'); }
}

// ── UTIL ──
function statusBadge(status) {
  const map = { ACTIVE: 'green', COMPLETED: 'blue', DROPPED: 'red' };
  return `<span class="badge badge-${map[status] || 'yellow'}">${status}</span>`;
}
