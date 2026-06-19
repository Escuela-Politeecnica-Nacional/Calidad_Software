<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi biblioteca digital - OwlShare</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Manrope:wght@700;800&family=Material+Symbols+Outlined&display=swap" rel="stylesheet">
    <script src="https://cdn.tailwindcss.com"></script>
    <style>body{font-family:Inter,sans-serif}h1,h2,h3{font-family:Manrope,sans-serif}.material-symbols-outlined{vertical-align:middle}</style>
</head>
<body class="min-h-screen bg-slate-50 text-slate-900">
<header class="border-b bg-white px-6 py-5">
    <div class="mx-auto flex max-w-6xl items-center justify-between">
        <div><p class="font-bold text-indigo-600">Espacio personal</p><h1 class="text-3xl font-extrabold">Mi biblioteca digital</h1></div>
        <div class="flex gap-3"><a href="${pageContext.request.contextPath}/estudiante/marketplace" class="rounded-xl bg-indigo-600 px-4 py-3 font-bold text-white">Explorar marketplace</a><a href="${pageContext.request.contextPath}/estudiante/dashboard" class="rounded-xl border px-4 py-3 font-bold">Inicio</a></div>
    </div>
</header>
<main class="mx-auto max-w-6xl p-6 md:p-10">
    <c:choose>
        <c:when test="${empty adquisiciones}">
            <section class="rounded-3xl border-2 border-dashed bg-white px-6 py-24 text-center">
                <span class="material-symbols-outlined text-7xl text-indigo-200">local_library</span>
                <h2 class="mt-5 text-2xl font-extrabold">Todavía no tienes materiales adquiridos</h2>
                <p class="mx-auto mt-2 max-w-lg text-slate-500">Cuando adquieras un documento aparecerá aquí para que puedas visualizarlo en cualquier momento.</p>
                <a href="${pageContext.request.contextPath}/estudiante/marketplace" class="mt-7 inline-block rounded-xl bg-indigo-600 px-6 py-3 font-bold text-white">Ir al marketplace</a>
            </section>
        </c:when>
        <c:otherwise>
            <section class="grid gap-5 md:grid-cols-2">
                <c:forEach var="adquisicion" items="${adquisiciones}">
                    <article class="rounded-2xl border bg-white p-6 shadow-sm">
                        <div class="flex items-start justify-between gap-4">
                            <span class="rounded-lg bg-emerald-50 px-3 py-1 text-xs font-bold text-emerald-700"><c:out value="${adquisicion.material.categoriaNombre}"/></span>
                            <span class="text-xs text-slate-400"><c:out value="${adquisicion.fechaAdquisicion}"/></span>
                        </div>
                        <h2 class="mt-4 text-xl font-extrabold"><c:out value="${adquisicion.material.titulo}"/></h2>
                        <dl class="mt-5 grid grid-cols-2 gap-4 text-sm">
                            <div><dt class="text-slate-400">Materia</dt><dd class="font-semibold"><c:out value="${adquisicion.material.nombreMateria}"/></dd></div>
                            <div><dt class="text-slate-400">Semestre</dt><dd class="font-semibold"><c:out value="${adquisicion.material.semestreEfectivo}"/></dd></div>
                            <div><dt class="text-slate-400">Tipo</dt><dd class="font-semibold">PDF</dd></div>
                            <div><dt class="text-slate-400">Tutor</dt><dd class="font-semibold"><c:out value="${adquisicion.material.usuario}"/></dd></div>
                        </dl>
                        <a href="${pageContext.request.contextPath}/estudiante/biblioteca/leer?id=${adquisicion.material.id}" class="mt-6 block rounded-xl bg-indigo-600 px-4 py-3 text-center font-bold text-white"><span class="material-symbols-outlined">menu_book</span> Visualizar documento</a>
                    </article>
                </c:forEach>
            </section>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
