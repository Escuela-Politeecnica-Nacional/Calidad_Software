<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- =============================================
     Vista: marketplace-materiales.jsp
     Servlet: MarketplaceMaterialServlet → GET /estudiante/marketplace
     Session: usuarioLogueado (Rol.ESTUDIANTE)
     ============================================= --%>
<!DOCTYPE html>
<html class="light" lang="es">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>Marketplace académico - OwlShare</title>
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
<body class="bg-surface text-on-surface min-h-screen flex">

<%-- Protección de ruta --%>
<c:if test="${empty sessionScope.usuarioLogueado}">
    <c:redirect url="/login"/>
</c:if>

<%-- ── Sidebar ── --%>
<aside class="hidden md:flex flex-col h-screen w-64 fixed left-0 top-0 bg-slate-50 py-6 space-y-4 z-50">
    <div class="px-6 mb-4">
        <h1 class="text-lg font-extrabold text-indigo-900 tracking-tight">OwlShare</h1>
        <p class="text-xs text-slate-500 font-medium">Portal de estudiante</p>
    </div>
    <nav class="flex-1 space-y-1 px-4">
        <a href="${pageContext.request.contextPath}/estudiante/dashboard"
           class="flex items-center gap-3 px-4 py-3 rounded-lg text-slate-500 hover:text-indigo-600 hover:bg-slate-100 transition-all">
            <span class="material-symbols-outlined">home</span>
            Inicio
        </a>
        <a href="${pageContext.request.contextPath}/estudiante/buscar-tutor"
           class="flex items-center gap-3 px-4 py-3 rounded-lg text-slate-500 hover:text-indigo-600 hover:bg-slate-100 transition-all">
            <span class="material-symbols-outlined">search</span>
            Buscar Tutor
        </a>
        <%-- Marketplace (activo) --%>
        <a href="${pageContext.request.contextPath}/estudiante/marketplace"
           class="flex items-center gap-3 px-4 py-3 rounded-lg text-indigo-700 font-bold border-r-4 border-indigo-600 bg-indigo-50/50 transition-all">
            <span class="material-symbols-outlined">storefront</span>
            Marketplace
        </a>
        <a href="${pageContext.request.contextPath}/estudiante/biblioteca"
           class="flex items-center gap-3 px-4 py-3 rounded-lg text-slate-500 hover:text-indigo-600 hover:bg-slate-100 transition-all">
            <span class="material-symbols-outlined">local_library</span>
            Mi Biblioteca
        </a>
        <a href="${pageContext.request.contextPath}/perfil"
           class="flex items-center gap-3 px-4 py-3 rounded-lg text-slate-500 hover:text-indigo-600 hover:bg-slate-100 transition-all">
            <span class="material-symbols-outlined">person</span>
            Mi Perfil
        </a>
    </nav>
    <div class="px-4 mt-auto">
        <a href="${pageContext.request.contextPath}/logout"
           class="flex items-center gap-3 px-4 py-3 rounded-lg text-slate-500 hover:text-error hover:bg-red-50 transition-all">
            <span class="material-symbols-outlined">logout</span>
            Cerrar Sesión
        </a>
    </div>
</aside>

<%-- ── Main ── --%>
<main class="flex-1 md:ml-64 min-h-screen bg-surface flex flex-col">

    <%-- Header --%>
    <header class="w-full sticky top-0 z-40 bg-white/80 backdrop-blur-md shadow-sm h-16 flex justify-between items-center px-8">
        <div class="flex items-center gap-4">
            <div class="p-2 rounded-lg bg-indigo-50">
                <span class="material-symbols-outlined text-primary">storefront</span>
            </div>
            <span class="hidden md:block text-xl font-bold text-indigo-900 tracking-tight" style="font-family:'Manrope',sans-serif">
                Marketplace académico
            </span>
        </div>
        <div class="flex items-center gap-4">
            <a href="${pageContext.request.contextPath}/estudiante/biblioteca"
               class="hidden sm:flex items-center gap-2 rounded-xl bg-primary px-4 py-2.5 font-bold text-on-primary hover:bg-primary-container transition-colors">
                <span class="material-symbols-outlined text-base">local_library</span>
                Mi biblioteca
            </a>
            <a href="${pageContext.request.contextPath}/logout"
               class="p-2 text-slate-600 hover:bg-red-50 hover:text-red-500 rounded-full transition-colors">
                <span class="material-symbols-outlined">logout</span>
            </a>
        </div>
    </header>

    <%-- Contenido principal --%>
    <div class="flex-1 p-6 md:p-10 pb-20 md:pb-10">
        <div class="max-w-5xl w-full mx-auto space-y-8">

            <header class="mb-2">
                <p class="text-sm font-semibold text-primary">Recursos aprobados</p>
                <h2 class="text-4xl md:text-5xl font-extrabold tracking-tight text-on-surface mb-2">
                    Marketplace académico
                </h2>
                <p class="text-lg text-on-surface-variant font-medium">
                    Explora materiales aprobados por carrera, materia y semestre.
                </p>
            </header>

            <c:if test="${not empty flashMensaje}">
                <div class="rounded-xl border border-emerald-200 bg-emerald-50 px-5 py-4 text-emerald-800"><c:out value="${flashMensaje}"/></div>
            </c:if>
            <c:if test="${not empty flashError}">
                <div class="rounded-xl border border-red-200 bg-red-50 px-5 py-4 text-red-800"><c:out value="${flashError}"/></div>
            </c:if>

            <form method="get" action="${pageContext.request.contextPath}/estudiante/marketplace"
                  class="grid gap-4 rounded-2xl border border-slate-100 bg-surface-container-lowest p-5 shadow-sm md:grid-cols-4">
                <label class="text-sm font-bold text-on-surface">Carrera
                    <select name="carrera" class="mt-2 w-full rounded-xl border-slate-200 bg-surface-container-high">
                        <option value="">Todas</option>
                        <c:forEach var="carrera" items="${carreras}">
                            <option value="${carrera}" ${carrera == carreraSeleccionada ? 'selected' : ''}><c:out value="${carrera.nombre}"/></option>
                        </c:forEach>
                    </select>
                </label>
                <label class="text-sm font-bold text-on-surface">Materia
                    <select name="materia" class="mt-2 w-full rounded-xl border-slate-200 bg-surface-container-high">
                        <option value="">Todas</option>
                        <c:forEach var="materia" items="${materias}">
                            <option value="${materia.codigo}" ${materia.codigo == materiaSeleccionada ? 'selected' : ''}><c:out value="${materia.nombre}"/></option>
                        </c:forEach>
                    </select>
                </label>
                <label class="text-sm font-bold text-on-surface">Semestre
                    <select name="semestre" class="mt-2 w-full rounded-xl border-slate-200 bg-surface-container-high">
                        <option value="">Todos</option>
                        <c:forEach var="semestre" items="${semestres}">
                            <option value="${semestre.numero}" ${semestre.numero == semestreSeleccionado ? 'selected' : ''}><c:out value="${semestre.nombre}"/></option>
                        </c:forEach>
                    </select>
                </label>
                <div class="flex items-end gap-2">
                    <button class="flex-1 rounded-xl bg-primary px-4 py-3 font-bold text-on-primary hover:bg-primary-container transition-colors">
                        <span class="material-symbols-outlined text-base">filter_alt</span> Filtrar
                    </button>
                    <a href="${pageContext.request.contextPath}/estudiante/marketplace" class="rounded-xl border border-slate-200 px-4 py-3 hover:bg-slate-50" title="Limpiar filtros">
                        <span class="material-symbols-outlined">restart_alt</span>
                    </a>
                </div>
            </form>

            <c:choose>
                <c:when test="${empty materiales}">
                    <section class="rounded-2xl border-2 border-dashed border-slate-200 bg-surface-container-lowest py-20 text-center">
                        <span class="material-symbols-outlined text-6xl text-slate-300">search_off</span>
                        <h2 class="mt-3 text-xl font-bold text-on-surface">No hay materiales disponibles para esta búsqueda</h2>
                        <p class="mt-2 text-on-surface-variant">Prueba cambiando o limpiando los filtros seleccionados.</p>
                    </section>
                </c:when>
                <c:otherwise>
                    <section class="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
                        <c:forEach var="material" items="${materiales}">
                            <article class="flex flex-col rounded-2xl border border-slate-100 bg-surface-container-lowest p-6 shadow-sm transition hover:-translate-y-1 hover:shadow-lg">
                                <div class="flex items-start justify-between">
                                    <span class="rounded-lg bg-indigo-50 px-3 py-1 text-xs font-bold uppercase text-indigo-700"><c:out value="${material.categoriaNombre}"/></span>
                                    <span class="text-lg font-extrabold text-emerald-700">$<c:out value="${material.costo}"/></span>
                                </div>
                                <h2 class="mt-5 text-xl font-extrabold text-on-surface"><c:out value="${material.titulo}"/></h2>
                                <p class="mt-2 line-clamp-3 text-sm leading-6 text-on-surface-variant"><c:out value="${material.descripcion}"/></p>
                                <dl class="mt-5 space-y-2 text-sm">
                                    <div class="flex justify-between gap-4"><dt class="text-slate-500">Carrera</dt><dd class="text-right font-semibold text-on-surface"><c:out value="${material.carreraEfectiva.nombre}"/></dd></div>
                                    <div class="flex justify-between gap-4"><dt class="text-slate-500">Materia</dt><dd class="text-right font-semibold text-on-surface"><c:out value="${material.nombreMateria}"/></dd></div>
                                    <div class="flex justify-between gap-4"><dt class="text-slate-500">Semestre</dt><dd class="font-semibold text-on-surface"><c:out value="${material.semestreEfectivo}"/></dd></div>
                                    <div class="flex justify-between gap-4"><dt class="text-slate-500">Tutor</dt><dd class="font-semibold text-on-surface"><c:out value="${material.usuario}"/></dd></div>
                                </dl>
                                <a href="${pageContext.request.contextPath}/estudiante/material/detalle?id=${material.id}"
                                   class="mt-6 rounded-xl bg-primary px-4 py-3 text-center font-bold text-on-primary hover:bg-primary-container transition-colors">
                                    Ver detalle y vista previa
                                </a>
                            </article>
                        </c:forEach>
                    </section>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <footer class="p-8 text-center text-slate-400 text-xs font-medium tracking-widest uppercase">
        © 2025 OwlShare · Plataforma Educativa Colaborativa
    </footer>
</main>

<%-- Mobile Bottom Nav --%>
<nav class="md:hidden fixed bottom-0 left-0 right-0 bg-white/95 backdrop-blur-lg border-t border-slate-100
            flex justify-around items-center h-16 px-4 z-50">
    <a href="${pageContext.request.contextPath}/estudiante/dashboard" class="flex flex-col items-center justify-center text-slate-400">
        <span class="material-symbols-outlined">home</span>
        <span class="text-[10px] font-bold mt-1 uppercase tracking-tighter">Inicio</span>
    </a>
    <a href="${pageContext.request.contextPath}/estudiante/marketplace" class="flex flex-col items-center justify-center text-indigo-700">
        <span class="material-symbols-outlined" style="font-variation-settings:'FILL' 1,'wght' 400,'GRAD' 0,'opsz' 24">storefront</span>
        <span class="text-[10px] font-bold mt-1 uppercase tracking-tighter">Materiales</span>
    </a>
    <a href="${pageContext.request.contextPath}/estudiante/biblioteca" class="flex flex-col items-center justify-center text-slate-400">
        <span class="material-symbols-outlined">local_library</span>
        <span class="text-[10px] font-bold mt-1 uppercase tracking-tighter">Biblioteca</span>
    </a>
    <a href="${pageContext.request.contextPath}/logout" class="flex flex-col items-center justify-center text-slate-400 hover:text-red-500">
        <span class="material-symbols-outlined">logout</span>
        <span class="text-[10px] font-bold mt-1 uppercase tracking-tighter">Salir</span>
    </a>
</nav>

</body>
</html>
