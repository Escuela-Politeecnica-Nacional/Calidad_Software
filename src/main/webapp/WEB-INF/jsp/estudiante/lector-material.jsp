<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${material.titulo} - OwlShare</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&family=Material+Symbols+Outlined&display=swap" rel="stylesheet">
    <script src="https://cdn.tailwindcss.com"></script>
    <style>body{font-family:Inter,sans-serif}.material-symbols-outlined{vertical-align:middle}img{-webkit-user-select:none;user-select:none}</style>
</head>
<body class="min-h-screen bg-slate-950 text-white" oncontextmenu="return false">
<header class="sticky top-0 z-20 border-b border-white/10 bg-slate-950/95 px-5 py-4 backdrop-blur">
    <div class="mx-auto flex max-w-7xl flex-wrap items-center justify-between gap-4">
        <div class="flex items-center gap-4">
            <a href="${pageContext.request.contextPath}/estudiante/biblioteca" class="rounded-lg p-2 hover:bg-white/10"><span class="material-symbols-outlined">arrow_back</span></a>
            <div><h1 class="font-bold">${material.titulo}</h1><p class="text-xs text-slate-400">${material.nombreMateria} · Visualización protegida</p></div>
        </div>
        <div class="flex items-center gap-3">
            <button id="anterior" class="rounded-lg border border-white/20 px-3 py-2 disabled:opacity-30"><span class="material-symbols-outlined">chevron_left</span></button>
            <span class="min-w-28 text-center text-sm">Página <strong id="paginaActual">1</strong> de ${totalPaginas}</span>
            <button id="siguiente" class="rounded-lg border border-white/20 px-3 py-2 disabled:opacity-30"><span class="material-symbols-outlined">chevron_right</span></button>
        </div>
    </div>
</header>
<main class="mx-auto flex max-w-7xl justify-center p-4 md:p-8">
    <div class="relative min-h-[70vh] w-full max-w-5xl rounded-xl bg-slate-800 p-3 text-center shadow-2xl">
        <div id="cargando" class="absolute inset-0 flex items-center justify-center bg-slate-800 text-slate-300">Cargando página…</div>
        <img id="paginaDocumento" src="${pageContext.request.contextPath}/estudiante/material/contenido?id=${material.id}&completo=true&pagina=1" alt="Página del documento" class="mx-auto max-w-full shadow-xl" draggable="false">
    </div>
</main>
<footer class="pb-8 text-center text-xs text-slate-500">El documento solo puede visualizarse dentro de OwlShare. La descarga está bloqueada.</footer>
<script>
    const total = ${totalPaginas};
    let pagina = 1;
    const imagen = document.getElementById('paginaDocumento');
    const cargando = document.getElementById('cargando');
    const anterior = document.getElementById('anterior');
    const siguiente = document.getElementById('siguiente');
    const indicador = document.getElementById('paginaActual');
    const base = '${pageContext.request.contextPath}/estudiante/material/contenido?id=${material.id}&completo=true&pagina=';

    function actualizar() {
        cargando.classList.remove('hidden');
        imagen.src = base + pagina;
        indicador.textContent = pagina;
        anterior.disabled = pagina <= 1;
        siguiente.disabled = pagina >= total;
    }
    imagen.addEventListener('load', () => cargando.classList.add('hidden'));
    anterior.addEventListener('click', () => { if (pagina > 1) { pagina--; actualizar(); } });
    siguiente.addEventListener('click', () => { if (pagina < total) { pagina++; actualizar(); } });
    document.addEventListener('keydown', e => {
        if ((e.ctrlKey || e.metaKey) && ['s','p'].includes(e.key.toLowerCase())) e.preventDefault();
        if (e.key === 'ArrowLeft' && pagina > 1) { pagina--; actualizar(); }
        if (e.key === 'ArrowRight' && pagina < total) { pagina++; actualizar(); }
    });
    actualizar();
</script>
</body>
</html>
