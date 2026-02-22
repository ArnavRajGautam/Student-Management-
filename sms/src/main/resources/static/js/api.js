// ─────────────────────────────────────────
// api.js — All backend communication
// Base URL: http://localhost:8080/api
// ─────────────────────────────────────────

const BASE_URL = 'http://localhost:8080/api';

async function request(method, path, body) {
  const res = await fetch(BASE_URL + path, {
    method,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: body ? JSON.stringify(body) : undefined,
  });

  const json = await res.json();
  if (!res.ok) throw new Error(json.message || `Error ${res.status}`);
  return json;
}

// Shorthand helpers
const api = {
  get:    (path)       => request('GET',    path),
  post:   (path, body) => request('POST',   path, body),
  put:    (path, body) => request('PUT',    path, body),
  patch:  (path, body) => request('PATCH',  path, body),
  delete: (path)       => request('DELETE', path),
};
