<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle del material - OwlShare</title>
    <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;600;700;800&family=Inter:wght@400;500;600;700&family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet">
    <script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
    <script>
        tailwind.config = {
            darkMode: "class",
            theme: { extend: { colors: {
                "surface": "#f7f9fc", "surface-container": "#eceef1",
                "surface-container-low": "#f2f4f7", "surface-container-lowest": "#ffffff",
                "surface-container-high": "#e6e8eb", "surface-container-highest": "#e0e3e6",
                "on-surface": "#191c1e", "on-surface-variant": "#454652",
                "primary": "#24389c", "primary-container": "#3f51b5",
                "primary-fixed": "#dee0ff", "on-primary": "#ffffff",
                "on-primary-fixed": "#00105c", "on-primary-fixed-variant": "#293ca0",
                "secondary": "#006a60", "secondary-container": "#85f6e5",
                "secondary-fixed": "#85f6e5", "on-secondary-container": "#007166",
                "tertiary-fixed": "#ffdcc6", "on-tertiary-fixed-variant": "#713700",
                "outline": "#757684", "outline-variant": "#c5c5d4",
                "error": "#ba1a1a", "background": "#f7f9fc"
            }}}
        }
    </script>
    <style>
        .material-symbols-outlined { font-variation-settings: 'FILL' 0, 'wght' 400, 'GRAD' 0, 'opsz' 24; vertical-align: middle; }
        body { font-family: 'Inter', sans-serif; }
        h1, h2, h3 { font-family: 'Manrope', sans-serif; }
    </style>
</head>
<body class="min-h-screen bg-surface text-on-surface">
<header class="border-b border-slate-100 bg-white/80 backdrop-blur-md px-6 py-4">
    <div class="mx-auto flex max-w-6xl items-center justify-between">
        <a href="${pageContext.request.contextPath}/estudiante/marketplace" class="flex items-center gap-1 font-bold text-primary"><span class="material-symbols-outlined">arrow_back</span> Marketplace</a>
        <div class="flex gap-3">
            <a href="${pageContext.request.contextPath}/estudiante/biblioteca" class="rounded-xl border border-slate-200 px-4 py-2 font-bold text-on-surface hover:bg-slate-50">Mi biblioteca</a>
            <a href="${pageContext.request.contextPath}/logout" class="rounded-xl px-4 py-2 text-slate-500 hover:text-red-500">Salir</a>
        </div>
    </div>
</header>

<main class="mx-auto max-w-6xl space-y-6 p-6 md:p-10">
    <c:if test="${not empty flashMensaje}"><div class="rounded-xl border border-emerald-200 bg-emerald-50 p-4 text-emerald-800"><c:out value="${flashMensaje}"/></div></c:if>
    <c:if test="${not empty flashError}"><div class="rounded-xl border border-red-200 bg-red-50 p-4 text-red-800"><c:out value="${flashError}"/></div></c:if>

    <section class="grid gap-8 lg:grid-cols-3">
        <div class="space-y-6 lg:col-span-2">
            <article class="rounded-2xl border border-slate-100 bg-surface-container-lowest p-7 shadow-sm">
                <div class="flex flex-wrap items-start justify-between gap-4">
                    <div>
                        <span class="rounded-lg bg-indigo-50 px-3 py-1 text-xs font-bold uppercase text-indigo-700"><c:out value="${material.categoriaNombre}"/></span>
                        <h1 class="mt-4 text-3xl font-extrabold text-on-surface"><c:out value="${material.titulo}"/></h1>
                        <p class="mt-2 text-on-surface-variant">Publicado el <c:out value="${material.fechaEnvio}"/></p>
                    </div>
                    <strong class="text-3xl text-emerald-700">$<c:out value="${material.costo}"/></strong>
                </div>
                <p class="mt-7 leading-7 text-on-surface-variant"><c:out value="${material.descripcion}"/></p>
                <dl class="mt-7 grid gap-4 border-t pt-6 sm:grid-cols-2">
                    <div><dt class="text-xs font-bold uppercase text-slate-400">Tipo</dt><dd class="mt-1 font-semibold"><c:out value="${material.categoriaNombre}"/> · PDF</dd></div>
                    <div><dt class="text-xs font-bold uppercase text-slate-400">Materia</dt><dd class="mt-1 font-semibold"><c:out value="${material.nombreMateria}"/></dd></div>
                    <div><dt class="text-xs font-bold uppercase text-slate-400">Carrera</dt><dd class="mt-1 font-semibold"><c:out value="${material.carreraEfectiva.nombre}"/></dd></div>
                    <div><dt class="text-xs font-bold uppercase text-slate-400">Semestre</dt><dd class="mt-1 font-semibold"><c:out value="${material.semestreEfectivo}"/></dd></div>
                </dl>
            </article>

            <article class="overflow-hidden rounded-2xl border bg-slate-900 p-4 shadow-sm">
                <div class="mb-3 flex items-center justify-between text-white">
                    <div><h2 class="font-bold">${adquirido ? 'Documento adquirido' : 'Vista previa limitada'}</h2><p class="text-xs text-slate-300">${adquirido ? 'Abre el lector para consultar todas las páginas.' : 'Solo se muestra la primera página.'}</p></div>
                    <span class="material-symbols-outlined">${adquirido ? 'verified' : 'lock'}</span>
                </div>
                <c:choose>
                    <c:when test="${vistaPreviaDisponible}">
                        <div class="flex max-h-[720px] justify-center overflow-auto rounded-xl bg-slate-200 p-3">
                            <img src="${pageContext.request.contextPath}/estudiante/material/contenido?id=${material.id}" alt="Primera página del material" class="max-w-full shadow-xl" draggable="false">
                        </div>
                    </c:when>
                    <c:otherwise><div class="rounded-xl bg-white p-12 text-center text-slate-600">La vista previa no está disponible para este formato.</div></c:otherwise>
                </c:choose>
            </article>
        </div>

        <aside class="space-y-5">
            <section class="rounded-2xl border border-slate-100 bg-surface-container-lowest p-6 shadow-sm">
                <p class="text-xs font-bold uppercase tracking-wider text-slate-400">Autor del material</p>
                <h2 class="mt-3 text-xl font-extrabold text-on-surface"><c:out value="${material.usuario}"/></h2>
                <c:if test="${not empty tutorMaterial}">
                    <p class="mt-1 text-sm text-on-surface-variant"><c:out value="${tutorMaterial.carrera.nombre}"/></p>
                    <a href="${pageContext.request.contextPath}/estudiante/tutor/perfil?id=${tutorMaterial.id}" class="mt-4 inline-flex font-bold text-primary">Ver perfil del tutor <span class="material-symbols-outlined">arrow_forward</span></a>
                </c:if>
            </section>

            <section class="rounded-2xl bg-primary-container p-6 text-white shadow-lg">
                <c:choose>
                    <c:when test="${adquirido}">
                        <span class="inline-flex rounded-full bg-emerald-400/20 px-3 py-1 text-xs font-bold text-emerald-200">YA ADQUIRIDO</span>
                        <h2 class="mt-4 text-xl font-extrabold">Disponible en tu biblioteca</h2>
                        <p class="mt-2 text-sm" style="color:rgba(222,224,255,0.85)">Puedes consultar el documento completo dentro de la plataforma.</p>
                        <a href="${pageContext.request.contextPath}/estudiante/biblioteca/leer?id=${material.id}" class="mt-5 block rounded-xl bg-white px-4 py-3 text-center font-bold text-primary">Abrir documento completo</a>
                    </c:when>
                    <c:otherwise>
                        <p class="text-sm" style="color:rgba(222,224,255,0.85)">Acceso completo al documento</p>
                        <div class="mt-2 text-4xl font-extrabold">$<c:out value="${material.costo}"/></div>
                        <form action="${pageContext.request.contextPath}/estudiante/material/adquirir" method="post" class="mt-5" onsubmit="return confirm('¿Confirmas la adquisición de este material?');">
                            <input type="hidden" name="id" value="${material.id}">
                            <button class="w-full rounded-xl bg-white px-4 py-3 font-bold text-primary">Adquirir material</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </section>
        </aside>
    </section>
</main>
</body>
</html>
