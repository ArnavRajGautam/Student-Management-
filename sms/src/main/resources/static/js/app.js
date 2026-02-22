// ─────────────────────────────────────────
// app.js — Router + Dashboard
// Entry point of the application
// ─────────────────────────────────────────

// ── PAGE ROUTER ──
const pages = { dashboard, students, courses, enrollments };

function switchPage(page) {
  // Update sidebar active state
  const navItems = document.querySelectorAll('.nav-item');
  const pageKeys = ['dashboard', 'students', 'courses', 'enrollments'];
  navItems.forEach((el, i) => el.classList.toggle('active', pageKeys[i] === page));

  // Clear and render page
  document.getElementById('main-content').innerHTML = '';
  pages[page]();
}

// ── DASHBOARD ──
async function dashboard() {
  document.getElementById('main-content').innerHTML = `
    <div class="page-header">
      <div>
        <div class="page-title">Dashboard</div>
        <div class="page-subtitle">Overview of your institution</div>
      </div>
    </div>
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">◎</div>
        <div class="stat-value" id="d-students">—</div>
        <div class="stat-label">Total Students</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">⬡</div>
        <div class="stat-value" id="d-courses">—</div>
        <div class="stat-label">Active Courses</div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">⟐</div>
        <div class="stat-value" id="d-enrollments">—</div>
        <div class="stat-label">Total Enrollments</div>
      </div>
    </div>
    <div class="section">
      <div class="tabs">
        <div class="tab active" onclick="switchDashTab(this, 'dash-students')">Recent Students</div>
        <div class="tab" onclick="switchDashTab(this, 'dash-enrollments')">Recent Enrollments</div>
      </div>
      <div id="dash-students" class="tab-content active">
        <div class="loading"><div class="spinner"></div>Loading...</div>
      </div>
      <div id="dash-enrollments" class="tab-content">
        <div class="loading"><div class="spinner"></div>Loading...</div>
      </div>
    </div>`;

  try {
    // Fetch all three in parallel
    const [sRes, cRes, eRes] = await Promise.all([
      api.get('/students'),
      api.get('/courses'),
      api.get('/enrollments'),
    ]);

    const studentList    = sRes.data || [];
    const courseList     = cRes.data || [];
    const enrollmentList = eRes.data || [];

    // Update stat cards
    document.getElementById('d-students').textContent    = studentList.length;
    document.getElementById('d-courses').textContent     = courseList.length;
    document.getElementById('d-enrollments').textContent = enrollmentList.length;

    // Update sidebar badges
    document.getElementById('student-count').textContent    = studentList.length;
    document.getElementById('course-count').textContent     = courseList.length;
    document.getElementById('enrollment-count').textContent = enrollmentList.length;

    // Recent Students (last 5)
    const recent = [...studentList].reverse().slice(0, 5);
    const dashStudentsEl = document.getElementById('dash-students');

    if (!recent.length) {
      dashStudentsEl.innerHTML = `<div class="empty"><div class="empty-icon">◎</div><div class="empty-text">No students yet</div></div>`;
    } else {
      dashStudentsEl.innerHTML = `
        <div class="table-wrap">
          <table>
            <thead><tr><th>Student</th><th>Email</th><th>Phone</th><th>Enrolled On</th></tr></thead>
            <tbody>
              ${recent.map(s => `
                <tr>
                  <td>
                    <div class="cell-student">
                      <div class="avatar">${initials(s.firstName, s.lastName)}</div>
                      <div class="cell-name">${s.firstName} ${s.lastName}</div>
                    </div>
                  </td>
                  <td style="font-family:'DM Mono',monospace;font-size:12px;color:var(--muted)">${s.email}</td>
                  <td style="color:var(--muted)">${s.phone || '—'}</td>
                  <td style="color:var(--muted);font-family:'DM Mono',monospace;font-size:12px">${s.enrollmentDate || '—'}</td>
                </tr>`).join('')}
            </tbody>
          </table>
        </div>`;
    }

    // Recent Enrollments (last 5)
    const recentE = [...enrollmentList].reverse().slice(0, 5);
    const dashEnrollEl = document.getElementById('dash-enrollments');

    if (!recentE.length) {
      dashEnrollEl.innerHTML = `<div class="empty"><div class="empty-icon">⟐</div><div class="empty-text">No enrollments yet</div></div>`;
    } else {
      dashEnrollEl.innerHTML = `
        <div class="table-wrap">
          <table>
            <thead><tr><th>Student</th><th>Course</th><th>Status</th><th>Grade</th></tr></thead>
            <tbody>
              ${recentE.map(e => `
                <tr>
                  <td style="font-weight:500">${e.studentName || `Student #${e.studentId}`}</td>
                  <td style="color:var(--muted)">${e.courseName || `Course #${e.courseId}`}</td>
                  <td>${statusBadge(e.status)}</td>
                  <td style="font-family:'DM Mono',monospace;color:var(--accent2)">${e.grade || '—'}</td>
                </tr>`).join('')}
            </tbody>
          </table>
        </div>`;
    }

  } catch (e) {
    toast('Could not connect to backend — is Spring Boot running on :8080?', 'error');
  }
}

function switchDashTab(el, targetId) {
  document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
  document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
  el.classList.add('active');
  document.getElementById(targetId).classList.add('active');
}

// ── BOOT ──
switchPage('dashboard');
