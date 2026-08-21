# Contrato de inyección de dependencias

## Grafo global

```text
AsistenciaApp (@HiltAndroidApp)
└── SingletonComponent
    ├── AppModule ────────────> Resources
    ├── DispatchersModule ────> CoroutineDispatchers
    ├── UiModule ─────────────> AudioHapticHelper
    └── RepositoryModule ─────> contratos Domain -> implementaciones Data

MainActivity (@AndroidEntryPoint)
└── AppNavHost
    └── hiltViewModel<BaseViewModel>()
        └── CoroutineDispatchers
```

## Decisiones

- `CoroutineDispatchers` vive en `core/coroutines`, no en `di`, para que los
  consumidores no dependan del composition root. En pruebas se reemplazan los
  tres dispatchers por `TestDispatcher`.
- Los módulos globales se instalan en `SingletonComponent`. El helper de audio
  y vibración usa exclusivamente `@ApplicationContext`, evitando conservar una
  `Activity` y sus ciclos de vida.
- Los ViewModels reciben casos de uso, repositorios o utilidades solo mediante
  constructor. Ningún ViewModel localiza dependencias manualmente.
- `ui` puede conocer `domain` y `core`, pero no `data` ni `di`; `data` puede
  conocer `domain` y `core`, pero no `ui` ni `di`. `di` es el único composition
  root y puede enlazar todas las capas.

## Patrón para próximos ViewModels

```kotlin
@HiltViewModel
class FeatureViewModel @Inject constructor(
    private val useCase: FeatureUseCase,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel()
```

Cada destino de Navigation Compose obtiene su instancia con `hiltViewModel()`.
El scope resultante queda asociado a la entrada del back stack, sin factories
manuales ni referencias globales.
