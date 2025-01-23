/**
// Obtiene referencia al formulario 
const form = document.getElementById('formIncidencia');

// Maneja el evento de envío del formulario
form.addEventListener('submit', async (event) => 
{
    event.preventDefault(); // Evitar el envío por defecto

    // Obtiene los valores de los campos
    const correoDocente = document.getElementById('correoDocente').value;
    const numeroAula = document.getElementById('numeroAula').value;
    const fechaInicio = document.getElementById('fechaInicio').value;
    const fechaFin = document.getElementById('fechaFin').value;
    const descripcionIncidencia = document.getElementById('descripcionIncidencia').value;
    const estadoIncidencia = document.getElementById('estadoIncidencia').value;
    const comentario = document.getElementById('comentario').value;

    // Valida los datos antes de enviarlos
    if (!correoDocente || !numeroAula || !descripcionIncidencia) 
	{
        alert('Por favor, complete todos los campos requeridos.');
        return;
    }

    // Crea un objeto con los datos del formulario
    const data = 
	{
        correoDocente: correoDocente,
        numeroAula: numeroAula,
        fechaInicio: fechaInicio,
        fechaFin: fechaFin,
        descripcionIncidencia: descripcionIncidencia,
        estadoIncidencia: estadoIncidencia,
        comentario: comentario || '', // Manejar el comentario opcional
    };

    try 
	{
        // Envia los datos a la API
        const response = await fetch('http://localhost:8888/incidencias/post', 
		{
            method: 'POST',
            headers
			{
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        // Manejo de la respuesta
        if (response.status === 201) 
		{
            alert('Incidencia creada con éxito.');
        } 
		else if (response.status === 200) 
		{
            alert('Incidencia actualizada con éxito.');
        } 
		else if (response.status === 400) 
		{
            const errorData = await response.json();
            alert(`Error: ${errorData.message || 'Datos inválidos.'}`);
        } 
		else if (response.status === 404) 
		{
            alert('No se encontró la incidencia.');
        } 
		else 
		{
            alert('Ocurrió un error inesperado. Intente nuevamente.');
        }
    } 
	catch (error) 
	{
        console.error('Error al conectarse a la API:', error);
        alert('Hubo un error al conectarse al servidor. Intenta nuevamente más tarde.');
    }
});
