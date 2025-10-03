import React from 'react'
import { useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { useMutation, useQueryClient } from 'react-query'
import { createCourse } from '../services/api'
import { BookOpen, Calendar, MapPin, Users, ArrowLeft, Plus } from 'lucide-react'
import toast from 'react-hot-toast'

/**
 * Create Course Page Component
 * Componente Pagina Crea Corso
 * 
 * Page for creating new courses
 * Pagina per creare nuovi corsi
 */
const CreateCourse = () => {
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  // Form handling with React Hook Form / Gestione form con React Hook Form
  const {
    register,
    handleSubmit,
    formState: { errors },
    watch
  } = useForm({
    defaultValues: {
      titolo: '',
      dataOraInizio: '',
      luogo: '',
      disponibilita: 20
    }
  })

  // Create course mutation / Mutazione crea corso
  const createCourseMutation = useMutation(createCourse, {
    onSuccess: (data) => {
      toast.success('Corso creato con successo!')
      queryClient.invalidateQueries('courses')
      navigate('/')
    },
    onError: (error) => {
      toast.error(error.message || 'Errore nella creazione del corso')
    }
  })

  // Handle form submission / Gestisce invio form
  const onSubmit = (data) => {
    createCourseMutation.mutate({
      titolo: data.titolo,
      dataOraInizio: data.dataOraInizio,
      luogo: data.luogo,
      disponibilita: parseInt(data.disponibilita)
    })
  }

  // Get current values for preview / Ottieni valori attuali per anteprima
  const watchedValues = watch()

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Header / Intestazione */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 flex items-center">
            <BookOpen className="w-8 h-8 mr-3 text-primary-600" />
            Nuovo Corso
          </h1>
          <p className="text-gray-600 mt-2">
            Crea un nuovo corso per il sistema di gestione
          </p>
        </div>
        
        <button
          onClick={() => navigate('/')}
          className="btn-secondary flex items-center"
        >
          <ArrowLeft className="w-4 h-4 mr-2" />
          Torna ai Corsi
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Course Form / Form Corso */}
        <div className="lg:col-span-2">
          <div className="card">
            <h2 className="text-xl font-semibold text-gray-800 mb-6 flex items-center">
              <Plus className="w-5 h-5 mr-2 text-primary-600" />
              Dettagli Corso
            </h2>

            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
              <div>
                <label className="form-label">Titolo Corso *</label>
                <div className="relative">
                  <BookOpen className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
                  <input
                    type="text"
                    className="form-input pl-10"
                    placeholder="Es. Introduzione a React"
                    {...register('titolo', {
                      required: 'Il titolo è obbligatorio',
                      maxLength: {
                        value: 50,
                        message: 'Il titolo non può superare i 50 caratteri'
                      }
                    })}
                  />
                </div>
                {errors.titolo && (
                  <p className="form-error">{errors.titolo.message}</p>
                )}
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="form-label">Data e Ora Inizio *</label>
                  <div className="relative">
                    <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
                    <input
                      type="datetime-local"
                      className="form-input pl-10"
                      {...register('dataOraInizio', {
                        required: 'La data di inizio è obbligatoria'
                      })}
                    />
                  </div>
                  {errors.dataOraInizio && (
                    <p className="form-error">{errors.dataOraInizio.message}</p>
                  )}
                </div>

                <div>
                  <label className="form-label">Posti Disponibili *</label>
                  <div className="relative">
                    <Users className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
                    <input
                      type="number"
                      min="1"
                      max="100"
                      className="form-input pl-10"
                      placeholder="20"
                      {...register('disponibilita', {
                        required: 'Il numero di posti è obbligatorio',
                        min: {
                          value: 1,
                          message: 'Il numero di posti deve essere almeno 1'
                        },
                        max: {
                          value: 100,
                          message: 'Il numero di posti non può superare 100'
                        }
                      })}
                    />
                  </div>
                  {errors.disponibilita && (
                    <p className="form-error">{errors.disponibilita.message}</p>
                  )}
                </div>
              </div>

              <div>
                <label className="form-label">Luogo *</label>
                <div className="relative">
                  <MapPin className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
                  <input
                    type="text"
                    className="form-input pl-10"
                    placeholder="Es. Aula Magna, Via Roma 123, Milano"
                    {...register('luogo', {
                      required: 'Il luogo è obbligatorio',
                      maxLength: {
                        value: 100,
                        message: 'Il luogo non può superare i 100 caratteri'
                      }
                    })}
                  />
                </div>
                {errors.luogo && (
                  <p className="form-error">{errors.luogo.message}</p>
                )}
              </div>

              <div className="flex justify-end space-x-4 pt-6 border-t">
                <button
                  type="button"
                  onClick={() => navigate('/')}
                  className="btn-secondary"
                >
                  Annulla
                </button>
                <button
                  type="submit"
                  className="btn-primary"
                  disabled={createCourseMutation.isLoading}
                >
                  {createCourseMutation.isLoading ? (
                    <>
                      <div className="loading-spinner w-4 h-4 mr-2"></div>
                      Creazione...
                    </>
                  ) : (
                    'Crea Corso'
                  )}
                </button>
              </div>
            </form>
          </div>
        </div>

        {/* Course Preview / Anteprima Corso */}
        <div className="lg:col-span-1">
          <div className="card">
            <h2 className="text-xl font-semibold text-gray-800 mb-4 flex items-center">
              <BookOpen className="w-5 h-5 mr-2 text-primary-600" />
              Anteprima Corso
            </h2>
            
            <div className="space-y-4">
              <div>
                <h3 className="font-medium text-gray-900 mb-2">Titolo</h3>
                <p className="text-sm text-gray-600">
                  {watchedValues.titolo || 'Titolo del corso'}
                </p>
              </div>

              <div>
                <h3 className="font-medium text-gray-900 mb-2">Data e Ora</h3>
                <p className="text-sm text-gray-600">
                  {watchedValues.dataOraInizio ? 
                    new Date(watchedValues.dataOraInizio).toLocaleDateString('it-IT', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                      hour: '2-digit',
                      minute: '2-digit'
                    }) : 
                    'Data e ora di inizio'
                  }
                </p>
              </div>

              <div>
                <h3 className="font-medium text-gray-900 mb-2">Luogo</h3>
                <p className="text-sm text-gray-600">
                  {watchedValues.luogo || 'Luogo del corso'}
                </p>
              </div>

              <div>
                <h3 className="font-medium text-gray-900 mb-2">Posti Disponibili</h3>
                <p className="text-sm text-gray-600">
                  {watchedValues.disponibilita || 0} posti
                </p>
              </div>

              <div className="mt-4 p-3 bg-blue-50 rounded-lg">
                <p className="text-xs text-blue-600">
                  💡 <strong>Suggerimento:</strong> Assicurati che tutti i campi siano compilati correttamente prima di creare il corso.
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CreateCourse
