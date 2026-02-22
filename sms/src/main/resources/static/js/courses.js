// ─────────────────────────────────────────
// courses.js — Courses page CRUD
// Endpoints used:
//   GET    /api/courses
//   GET    /api/courses/:id
//   POST   /api/courses
//   PUT    /api/courses/:id
//   DELETE /api/courses/:id
// ─────────────────────────────────────────

let allCourses = [];

// ── RENDER PAGE ──
async function courses() {
  document.getElementById('main-content').innerHTML = `
    <div class="page-header">
      <div>
        <div class="page-title">Courses</div>
        <div class="page-subtitle">Manage course catalog</div>
      </div>
      <button class="btn btn-primary" onclick="openAddCourse()">+ Add Course</button>
    </div>
    <div class="section">
      <div class="toolbar">
        <div class="search-box">
          <span class="search-icon">⌕</span>
          <input type="text" placeholder="Search by name, code, or instructor..." oninput="filterCourses(this.value)" />
        </div>
        <button class="btn btn-ghost" onclick="loadCourses()">↻ Refresh</button>
      </div>
      <div id="course-table">
        <div class="loading"><div class="spinner"></div>Loading courses...</div>
      </div>
    </div>`;

  await loadCourses();
}

// ── LOAD ALL ──
async function loadCourses() {
  try {
    const res = await api.get('/courses');
    allCourses = res.data || [];
    document.getElementById('course-count').textContent = allCourses.length;
    renderCourseTable(allCourses);
  } catch (e) {
    document.getElementById('course-table').innerHTML =
      `<div class="empty"><div class="empty-icon">✕</div><div class="empty-text">${e.message}</div></div>`;
    toast(e.message, 'error');
  }
}

// ── FILTER ──
function filterCourses(query) {
  const q = query.toLowerCase();
  const filtered = allCourses.filter(c =>
    c.courseName?.toLowerCase().includes(q) ||
    c.courseCode?.toLowerCase().includes(q) ||
    c.instructor?.toLowerCase().includes(q)
  );
  renderCourseTable(filtered);
}

// ── RENDER TABLE ──
function renderCourseTable(data) {
  const el = document.getElementById('course-table');
  if (!data.length) {
    el.innerHTML = `<div class="empty"><div class="empty-icon">⬡</div><div class="empty-text">No courses found</div></div>`;
    return;
  }
  el.innerHTML = `
    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Course Name</th>
            <th>Code</th>
            <th>Credits</th>
            <th>Instructor</th>
            <th>Description</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          ${data.map(c => `
            <tr>
              <td><div class="cell-name">${c.courseName}</div></td>
              <td><span class="badge badge-blue">${c.courseCode}</span></td>
              <td style="font-family:'DM Mono',monospace;color:var(--accent2)">${c.credits}</td>
              <td style="color:var(--muted)">${c.instructor || '—'}</td>
              <td style="color:var(--muted);font-size:12px;max-width:200px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">
                ${c.description || '—'}
              </td>
              <td>
                <div class="actions">
                  <button class="btn btn-ghost btn-sm" onclick="openEditCourse(${c.id})">Edit</button>
                  <button class="btn btn-danger btn-sm" onclick="confirmDeleteCourse(${c.id}, '${c.courseName}')">Delete</button>
                </div>
              </td>
            </tr>`).join('')}
        </tbody>
      </table>
    </div>`;
}

// ── FORM HTML ──
function courseFormHTML(c = {}) {
  return `
    <div class="form-grid">
      <div class="form-group">
        <label>Course Name *</label>
        <input id="c-name" value="${c.courseName || ''}" placeholder="Data Structures" />
      </div>
      <div class="form-group">
        <label>Course Code *</label>
        <input id="c-code" value="${c.courseCode || ''}" placeholder="CS101" />
      </div>
      <div class="form-group">
        <label>Credits *</label>
        <input id="c-credits" type="number" value="${c.credits || ''}" placeholder="4" min="1" max="10" />
      </div>
      <div class="form-group">
        <label>Instructor</label>
        <input id="c-instructor" value="${c.instructor || ''}" placeholder="Dr. Smith" />
      </div>
      <div class="form-group full">
        <label>Description</label>
        <textarea id="c-desc" placeholder="Course description...">${c.description || ''}</textarea>
      </div>
    </div>`;
}

// ── COLLECT FORM DATA ──
function getCourseFormData() {
  return {
    courseName:  document.getElementById('c-name').value.trim(),
    courseCode:  document.getElementById('c-code').value.trim(),
    credits:     parseInt(document.getElementById('c-credits').value) || null,
    instructor:  document.getElementById('c-instructor').value.trim() || null,
    description: document.getElementById('c-desc').value.trim() || null,
  };
}

// ── ADD ──
function openAddCourse() {
  openModal('Add Course', courseFormHTML(), `
    <button class="btn btn-ghost" onclick="closeModal()">Cancel</button>
    <button class="btn btn-primary" onclick="submitAddCourse()">Create Course</button>`);
}

async function submitAddCourse() {
  const data = getCourseFormData();
  if (!data.courseName || !data.courseCode || !data.credits) {
    toast('Please fill all required fields', 'error'); return;
  }
  try {
    await api.post('/courses', data);
    toast('Course created!', 'success');
    closeModal();
    await loadCourses();
  } catch (e) { toast(e.message, 'error'); }
}

// ── EDIT ──
async function openEditCourse(id) {
  try {
    const res = await api.get(`/courses/${id}`);
    openModal('Edit Course', courseFormHTML(res.data), `
      <button class="btn btn-ghost" onclick="closeModal()">Cancel</button>
      <button class="btn btn-primary" onclick="submitEditCourse(${id})">Save Changes</button>`);
  } catch (e) { toast(e.message, 'error'); }
}

async function submitEditCourse(id) {
  try {
    await api.put(`/courses/${id}`, getCourseFormData());
    toast('Course updated!', 'success');
    closeModal();
    await loadCourses();
  } catch (e) { toast(e.message, 'error'); }
}

// ── DELETE ──
function confirmDeleteCourse(id, name) {
  openModal('Delete Course',
    `<p style="color:var(--muted);font-size:14px">
      Delete <strong style="color:var(--text)">${name}</strong>? This cannot be undone.
    </p>`, `
    <button class="btn btn-ghost" onclick="closeModal()">Cancel</button>
    <button class="btn btn-danger" onclick="submitDeleteCourse(${id})">Delete</button>`);
}

async function submitDeleteCourse(id) {
  try {
    await api.delete(`/courses/${id}`);
    toast('Course deleted', 'success');
    closeModal();
    await loadCourses();
  } catch (e) { toast(e.message, 'error'); }
}
