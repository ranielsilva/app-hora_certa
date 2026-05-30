# Documentação Técnica - App Hora Certa (MVP)

## 1. Introdução
O **Hora Certa** é um aplicativo de saúde digital focado no gerenciamento de medicamentos, consultas e lembretes de saúde. O objetivo principal é auxiliar usuários na adesão ao tratamento medicamentoso através de uma interface simples, acessível e segura.

## 2. Arquitetura e Tecnologias
- **Linguagem:** Java
- **Plataforma:** Android SDK
- **Persistência:** SQLite (Banco de dados local) e SharedPreferences (Configurações de fluxo)
- **UI/UX:** Material Design 3, CoordinatorLayout, RecyclerView (Lista otimizada)
- **Notificações:** AlarmManager + BroadcastReceiver para lembretes em tempo real.

## 3. Funcionalidades Implementadas
### 3.1. Fluxo de Boas-vindas (Onboarding)
- **Splash Screen:** Tela de carregamento com animação de fade-in.
- **LGPD:** Tela de consentimento de dados e termos de uso.
- **Perfil:** Coleta de apelido, sexo e ano de nascimento para personalização.

### 3.2. Tela Inicial (Dashboard)
- **Calendário Horizontal:** Exibição dinâmica da data atual e dias adjacentes.
- **Lista de Medicamentos:** Gerenciamento visual com indicadores de status (Tomado/Pendente).
- **Botão Flutuante (FAB):** Acesso rápido a novas ações via menu Material.

### 3.3. Cadastros
- **Medicamentos:** Fluxo em etapas (Nome -> Frequência -> Horário -> Tratamento).
- **Consultas Médicas:** Registro de especialidade, médico, data, hora e local.
- **Lembretes:** Título e descrição para cuidados gerais de saúde.

## 4. Estrutura de Dados (SQLite)
O banco de dados `HoraCerta.db` contém as seguintes tabelas:
- `medicamentos`: Armazena a prescrição do usuário.
- `historico`: Log de auditoria de quando cada remédio foi efetivamente tomado.
- `consultas`: Agenda de compromissos médicos.
- `lembretes`: Alarmes genéricos de saúde.

## 5. Segurança e Privacidade (LGPD)
O aplicativo foi desenvolvido sob o conceito de *Privacy by Design*:
- **Local Storage:** 100% dos dados são armazenados no dispositivo (SQLite).
- **Sem Cloud:** Não há envio de dados sensíveis para servidores externos.
- **Consentimento:** Fluxo inicial bloqueado até que o usuário aceite os termos.

## 6. Requisitos de Execução
- **Permissões:** Notificações (`POST_NOTIFICATIONS`) e Alarmes Exatos (`SCHEDULE_EXACT_ALARM`).
- **Versão mínima:** Compatível com Android 8.0 (API 26) ou superior.

---
*Documentação gerada automaticamente para fins de avaliação de MVP.*
