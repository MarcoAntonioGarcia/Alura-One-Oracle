// API Base URL
const API_BASE = 'http://localhost:8080';

// Elementos comunes
const btnLogout = document.getElementById('btnLogout');

if (btnLogout) {
    btnLogout.addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.removeItem('jwtToken');
        window.location.href = 'index.html';
    });
}

/**
 * Módulo de Autenticación
 */
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const login = document.getElementById('loginInput').value;
        const clave = document.getElementById('passwordInput').value;
        const alertBox = document.getElementById('loginAlert');

        try {
            const response = await fetch(`${API_BASE}/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ login, clave })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('jwtToken', data.jwtToken); // Guardar token
                window.location.href = 'dashboard.html'; // Redirigir
            } else {
                alertBox.classList.remove('d-none');
                alertBox.textContent = 'Credenciales incorrectas. Intenta de nuevo.';
            }
        } catch (error) {
            console.error('Error in login:', error);
            alertBox.classList.remove('d-none');
            alertBox.textContent = 'Error de conexión con el servidor.';
        }
    });

    // Si ya tiene token y está en login, ir a dashboard
    if (localStorage.getItem('jwtToken')) {
        window.location.href = 'dashboard.html';
    }
}

/**
 * Función para verificar que el usuario esté logueado
 */
function verificarAutenticacion() {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        window.location.href = 'index.html'; // Echar al usuario si no hay token
    }
    return token;
}

/**
 * Función genérica para hacer peticiones Fetch inyectando el Token
 */
async function fetchWithToken(endpoint, options = {}) {
    const token = verificarAutenticacion();
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        ...(options.headers || {})
    };

    return await fetch(`${API_BASE}${endpoint}`, { ...options, headers });
}

/**
 * Módulo de Dashboard (Listar Tópicos)
 */
async function cargarTopicos(page = 0, size = 10) {
    const tbody = document.getElementById('topicosTableBody');
    if (!tbody) return;

    try {
        const response = await fetchWithToken(`/topicos?page=${page}&size=${size}&sort=fechaCreacion,desc`);

        if (response.ok) {
            const data = await response.json();
            renderTable(data.content);
            renderPagination(data);
        } else if (response.status === 403) {
            localStorage.removeItem('jwtToken');
            window.location.href = 'index.html';
        }
    } catch (error) {
        console.error('Error fetching topics:', error);
        tbody.innerHTML = `<tr><td colspan="8" class="text-danger">Error cargando los tópicos.</td></tr>`;
    }
}

function renderTable(topicos) {
    const tbody = document.getElementById('topicosTableBody');
    tbody.innerHTML = '';

    if (topicos.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" class="text-center text-muted py-4"><i class="bi bi-inbox fs-2 d-block mb-2"></i>No hay tópicos registrados.</td></tr>`;
        return;
    }

    topicos.forEach(topico => {
        const fechaFormat = new Date(topico.fechaCreacion).toLocaleString('es-ES');
        let badgeColor = topico.status === 'NO_RESPONDIDO' ? 'bg-warning text-dark' : 'bg-success';

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="fw-bold text-muted">#${topico.id}</td>
            <td class="text-start fw-semibold">${topico.titulo}</td>
            <td class="text-start text-truncate" style="max-width: 200px;">${topico.mensaje}</td>
            <td><span class="badge bg-secondary"><i class="bi bi-person me-1"></i>${topico.autor}</span></td>
            <td>${topico.curso}</td>
            <td class="small text-muted">${fechaFormat}</td>
            <td><span class="badge ${badgeColor}">${topico.status}</span></td>
            <td>
                <a href="formulario.html?id=${topico.id}" class="btn btn-sm btn-outline-primary me-1" title="Editar"><i class="bi bi-pencil"></i></a>
                <button onclick="eliminarTopico(${topico.id})" class="btn btn-sm btn-outline-danger" title="Eliminar"><i class="bi bi-trash"></i></button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function renderPagination(pageData) {
    const ul = document.getElementById('pagination');
    if (!ul) return;
    ul.innerHTML = '';

    // Botón Previo
    let prevClass = pageData.first ? 'disabled' : '';
    let prevIndex = pageData.number - 1;
    ul.innerHTML += `<li class="page-item ${prevClass}"><a class="page-link" href="#" onclick="cargarTopicos(${prevIndex})">Anterior</a></li>`;

    // Numeros
    for (let i = 0; i < pageData.totalPages; i++) {
        let activeClass = i === pageData.number ? 'active' : '';
        ul.innerHTML += `<li class="page-item ${activeClass}"><a class="page-link" href="#" onclick="cargarTopicos(${i})">${i + 1}</a></li>`;
    }

    // Botón Siguiente
    let nextClass = pageData.last ? 'disabled' : '';
    let nextIndex = pageData.number + 1;
    ul.innerHTML += `<li class="page-item ${nextClass}"><a class="page-link" href="#" onclick="cargarTopicos(${nextIndex})">Siguiente</a></li>`;
}

async function eliminarTopico(id) {
    if (confirm(`¿Estás seguro de que deseas eliminar el tópico #${id}?`)) {
        try {
            const response = await fetchWithToken(`/topicos/${id}`, { method: 'DELETE' });
            if (response.ok) {
                showAlert('success', 'Tópico eliminado correctamente.');
                cargarTopicos(); // Recargar
            } else {
                showAlert('danger', 'Error al eliminar el tópico.');
            }
        } catch (error) {
            showAlert('danger', 'Error de red.');
        }
    }
}

/**
 * Módulo de Formulario (Crear/Editar)
 */
const topicoForm = document.getElementById('topicoForm');
if (topicoForm) {
    topicoForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const id = document.getElementById('topicoId').value;
        const isEdit = id !== '';

        const btnSubmit = document.getElementById('btnSubmit');
        btnSubmit.disabled = true;
        btnSubmit.innerHTML = `<span class="spinner-border spinner-border-sm me-2" role="status"></span>Guardando...`;

        const payload = {
            titulo: document.getElementById('tituloInput').value,
            mensaje: document.getElementById('mensajeInput').value,
            autor: document.getElementById('autorInput').value,
            curso: document.getElementById('cursoInput').value
        };

        const method = isEdit ? 'PUT' : 'POST';
        const url = isEdit ? `/topicos/${id}` : `/topicos`;

        // If edit, Remove autor y curso ya que el DTO DatosActualizarTopico no los soporta
        if (isEdit) {
            delete payload.autor;
            delete payload.curso;
        }

        try {
            const response = await fetchWithToken(url, {
                method: method,
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const formAlertElement = document.getElementById('formAlert');
                formAlertElement.classList.remove('d-none', 'alert-danger');
                formAlertElement.classList.add('alert-success');
                formAlertElement.innerHTML = '<i class="bi bi-check-circle me-2"></i> Tópico ' + (isEdit ? 'actualizado' : 'creado') + ' correctamente. Redirigiendo...';

                setTimeout(() => { window.location.href = 'dashboard.html'; }, 1500);
            } else {
                const errorData = await response.json();
                mostrarErrorFormulario(errorData.mensaje || 'Error de validación al guardar.');
            }
        } catch (error) {
            mostrarErrorFormulario('Fallo la comunicación con el servidor.');
        } finally {
            btnSubmit.disabled = false;
            btnSubmit.innerHTML = `<i class="bi bi-save me-1"></i> Guardar Tópico`;
        }
    });
}

async function cargarDatosTopico(id) {
    try {
        const response = await fetchWithToken(`/topicos/${id}`);
        if (response.ok) {
            const topico = await response.json();
            document.getElementById('formTitle').innerHTML = `<i class="bi bi-pencil me-2"></i> Editar Tópico #${id}`;
            document.getElementById('topicoId').value = topico.id;
            document.getElementById('tituloInput').value = topico.titulo;
            document.getElementById('mensajeInput').value = topico.mensaje;

            // Auto and Course are read only on update, keep fields disabled
            const inputAutor = document.getElementById('autorInput');
            const inputCurso = document.getElementById('cursoInput');
            inputAutor.value = topico.autor;
            inputAutor.disabled = true;
            inputCurso.value = topico.curso;
            inputCurso.disabled = true;

        } else {
            alert('Tópico no encontrado');
            window.location.href = 'dashboard.html';
        }
    } catch (err) {
        alert('Error cargando datos del tópico');
    }
}

function mostrarErrorFormulario(msg) {
    const formAlertElement = document.getElementById('formAlert');
    formAlertElement.classList.remove('d-none', 'alert-success');
    formAlertElement.classList.add('alert-danger');
    formAlertElement.innerHTML = `<i class="bi bi-exclamation-triangle me-2"></i> ${msg}`;
}

/**
 * Utilidades
 */
function showAlert(type, message) {
    const container = document.getElementById('alertContainer');
    if (container) {
        container.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show shadow-sm" role="alert">
            ${type === 'success' ? '<i class="bi bi-check-circle-fill me-2"></i>' : '<i class="bi bi-exclamation-triangle-fill me-2"></i>'}
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>`;
        setTimeout(() => { container.innerHTML = ''; }, 4000);
    }
}
