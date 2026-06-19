<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Marketplace académico - OwlShare</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Manrope:wght@700;800&family=Material+Symbols+Outlined&display=swap" rel="stylesheet">
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        body { font-family: Inter, sans-serif; }
        h1,h2,h3 { font-family: Manrope, sans-serif; }
        .material-symbols-outlined { vertical-align: middle; }
    </style>
</head>
<body class="min-h-screen bg-slate-50 text-slate-900">
<aside class="fixed inset-y-0 left-0 hidden w-64 flex-col bg-indigo-950 p-5 text-white md:flex">
    <h1 class="px-3 text-xl font-extrabold">OwlShare</h1>
    <p class="px-3 text-xs text-indigo-200">Portal del estudiante</p>
    <nav class="mt-8 space-y-2">
        <a href="${pageContext.request.contextPath}/estudiante/dashboard" class="flex gap-3 rounded-xl px-4 py-3 text-indigo-100 hover:bg-white/10"><span class="material-symbols-outlined">home</span>Inicio</a>
        <a href="${pageContext.request.contextPath}/estudiante/marketplace" class="flex gap-3 rounded-xl bg-white px-4 py-3 font-bold text-indigo-950"><span class="material-symbols-outlined">storefront</span>Marketplace</a>
        <a href="${pageContext.request.contextPath}/estudiante/biblioteca" class="flex gap-3 rounded-xl px-4 py-3 text-indigo-100 hover:bg-white/10"><span class="material-symbols-outlined">local_library</span>Mi biblioteca</a>
        <a href="${pageContext.request.contextPath}/estudiante/buscar-tutor" class="flex gap-3 rounded-xl px-4 py-3 text-indigo-100 hover:bg-white/10"><span class="material-symbols-outlined">person_search</span>Buscar tutor</a>
    </nav>
    <a href="${pageContext.request.contextPath}/logout" class="mt-auto flex gap-3 rounded-xl px-4 py-3 text-indigo-100 hover:bg-white/10"><span class="material-symbols-outlined">logout</span>Cerrar sesión</a>
</aside>

<main class="min-h-screen md:ml-64">
    <header class="border-b bg-white px-6 py-5 md:px-10">
        <div class="mx-auto flex max-w-7xl items-center justify-between">
            <div>
                <p class="text-sm font-semibold text-indigo-600">Recursos aprobados</p>
                <h1 class="text-3xl font-extrabold">Marketplace académico</h1>
            </div>
            <a href="${pageContext.request.contextPath}/estudiante/biblioteca" class="rounded-xl bg-indigo-600 px-4 py-2.5 font-bold text-white">Mi biblioteca</a>
        </div>
    </header>

    <div class="mx-auto max-w-7xl space-y-7 p-6 md:p-10">
        <c:if test="${not empty flashMensaje}">
            <div class="rounded-xl border border-emerald-200 bg-emerald-50 px-5 py-4 text-emerald-800"><c:out value="${flashMensaje}"/></div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="rounded-xl border border-red-200 bg-red-50 px-5 py-4 text-red-800"><c:out value="${flashError}"/></div>
        </c:if>

        <form method="get" action="${pageContext.request.contextPath}/estudiante/marketplace" class="grid gap-4 rounded-2xl border bg-white p-5 shadow-sm md:grid-cols-4">
            <label class="text-sm font-bold">Carrera
                <select name="carrera" class="mt-2 w-full rounded-xl border-slate-200">
                    <option value="">Todas</option>
                    <c:forEach var="carrera" items="${carreras}">
                        <option value="${carrera}" ${carrera == carreraSeleccionada ? 'selected' : ''}><c:out value="${carrera.nombre}"/></option>
                    </c:forEach>
                </select>
            </label>
            <label class="text-sm font-bold">Materia
                <select name="materia" class="mt-2 w-full rounded-xl border-slate-200">
                    <option value="">Todas</option>
                    <c:forEach var="materia" items="${materias}">
                        <option value="${materia.codigo}" ${materia.codigo == materiaSeleccionada ? 'selected' : ''}><c:out value="${materia.nombre}"/></option>
                    </c:forEach>
                </select>
            </label>
            <label class="text-sm font-bold">Semestre
                <select name="semestre" class="mt-2 w-full rounded-xl border-slate-200">
                    <option value="">Todos</option>
                    <c:forEach var="semestre" items="${semestres}">
                        <option value="${semestre.numero}" ${semestre.numero == semestreSeleccionado ? 'selected' : ''}><c:out value="${semestre.nombre}"/></option>
                    </c:forEach>
                </select>
            </label>
            <div class="flex items-end gap-2">
                <button class="flex-1 rounded-xl bg-indigo-600 px-4 py-3 font-bold text-white"><span class="material-symbols-outlined text-base">filter_alt</span> Filtrar</button>
                <a href="${pageContext.request.contextPath}/estudiante/marketplace" class="rounded-xl border px-4 py-3" title="Limpiar filtros"><span class="material-symbols-outlined">restart_alt</span></a>
            </div>
        </form>

        <c:choose>
            <c:when test="${empty materiales}">
                <section class="rounded-2xl border-2 border-dashed bg-white py-20 text-center">
                    <span class="material-symbols-outlined text-6xl text-slate-300">search_off</span>
                    <h2 class="mt-3 text-xl font-bold">No hay materiales disponibles para esta búsqueda</h2>
                    <p class="mt-2 text-slate-500">Prueba cambiando o limpiando los filtros seleccionados.</p>
                </section>
            </c:when>
            <c:otherwise>
                <section class="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
                    <c:forEach var="material" items="${materiales}">
                        <article class="flex flex-col rounded-2xl border bg-white p-6 shadow-sm transition hover:-translate-y-1 hover:shadow-lg">
                            <div class="flex items-start justify-between">
                                <span class="rounded-lg bg-indigo-50 px-3 py-1 text-xs font-bold uppercase text-indigo-700"><c:out value="${material.categoriaNombre}"/></span>
                                <span class="text-lg font-extrabold text-emerald-700">$<c:out value="${material.costo}"/></span>
                            </div>
                            <h2 class="mt-5 text-xl font-extrabold"><c:out value="${material.titulo}"/></h2>
                            <p class="mt-2 line-clamp-3 text-sm leading-6 text-slate-600"><c:out value="${material.descripcion}"/></p>
                            <dl class="mt-5 space-y-2 text-sm">
                                <div class="flex justify-between gap-4"><dt class="text-slate-500">Carrera</dt><dd class="text-right font-semibold"><c:out value="${material.carreraEfectiva.nombre}"/></dd></div>
                                <div class="flex justify-between gap-4"><dt class="text-slate-500">Materia</dt><dd class="text-right font-semibold"><c:out value="${material.nombreMateria}"/></dd></div>
                                <div class="flex justify-between gap-4"><dt class="text-slate-500">Semestre</dt><dd class="font-semibold"><c:out value="${material.semestreEfectivo}"/></dd></div>
                                <div class="flex justify-between gap-4"><dt class="text-slate-500">Tutor</dt><dd class="font-semibold"><c:out value="${material.usuario}"/></dd></div>
                            </dl>
                            <a href="${pageContext.request.contextPath}/estudiante/material/detalle?id=${material.id}" class="mt-6 rounded-xl bg-indigo-600 px-4 py-3 text-center font-bold text-white">Ver detalle y vista previa</a>
                        </article>
                    </c:forEach>
                </section>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>
