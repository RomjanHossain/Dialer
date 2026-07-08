# Design System & UI Philosophy

## 1. Core Design Language
We deliberately **avoid Material Design** and instead follow a custom design language inspired by **iOS Human Interface Guidelines** and **Samsung One UI**. The result is a crisp, translucent, motion-rich interface that feels premium and native to high-end devices.

### Guiding Principles
- **Depth & Translucency:** Use frosted glass, dynamic blur, and layered surfaces.
- **One‑Handed Reachability:** Key actions gravitate toward the lower half of the screen (Samsung One UI style).
- **Motion as Information:** Animations guide attention, not distract. Every transition has a purpose.
- **Typographic Clarity:** Strong hierarchy through weight and size, never overusing capitals or decorative fonts.
- **Adaptive Color:** Deep integration with device light/dark modes, subtle gradients, and a restrained accent palette.

## 2. Visual Foundations

### Typography
- **Font Family:** System default (Roboto on stock Android, Samsung One UI font on Galaxy devices). No custom fonts to keep native feel.
- **Scale:** Tight modular scale (12sp caption → 16sp body → 20sp title → 28sp large display). All sizes use `sp` for accessibility.
- **Weight Strategy:** Regular for body, Medium for emphasis, SemiBold for headings, Bold for key actions. Avoid Light weight except in very large hero text.

### Color System
- **Light Theme:** Cool white/ice base, soft gray frosted overlays, vibrant but elegant accent (deep blue or teal, never pure Material blue).
- **Dark Theme:** True black (`#000000`) for OLED power saving on dialer screen, dark gray for cards, accent shifts to a luminous hue.
- **Gradients:** Used sparingly – a subtle linear gradient on the active call screen background, animated slowly.
- **Recording Red:** A specific deep crimson (`#D32F2F` or softer `#E53935`) with glowing animation, never the default Material red.

### Surfaces & Depth
- **Background:** Solid neutral color (light: `#F2F2F7`, dark: `#000000`). No wallpaper blurring except on the in-call screen.
- **Cards & Sheets:** Rounded corners (16–20dp), semi-transparent with `Blur` modifier (using `RenderEffect` or `Modifier.blur()` on Android 12+). Fallback to semi‑opaque with a subtle shadow.
- **Shadows:** Layered, tinted shadows (not Material’s single-elevation shadow). Use custom `Modifier.shadow()` with colored ambient + spot shadows.

### Iconography
- **Style:** Thin, stroke‑based, geometric icons (similar to SF Symbols). Line width 1.5–2dp.
- **Source:** Custom vector drawables, no Material icon pack. Feel free to use small filled icons only for active states (e.g., selected tab).
- **Size:** Consistent 24dp tap targets, with icon inside 44dp touch area.

## 3. Key Components (Custom, Non‑Material)

### Dialpad
- **Buttons:** Large, pill‑shaped or slightly rounded rectangles (48dp height, 72dp width), soft shadow, spring‑loaded press animation.
- **Feedback:** Haptic (Taptic‑Engine‑like) on each press, subtle click sound (user‑toggleable).
- **Number Display:** Large, monospaced digits at the top, with smooth typing animation (characters fade/slide in).

### Tabs (Recents, Contacts, Keypad, Recordings)
- **Indicator:** A small, animated pill that slides between tabs, not the standard Material underline. It uses a blur trail.
- **Labels:** Icon + text, vertically stacked or side‑by‑side depending on screen width.
- **Transition:** Horizontal swipe between tabs with a shared x-axis translation and fade, no page‑turn effect.

### In‑Call UI
- **Layout:** Floating, repositionable call card (minimal). Contact avatar with a dynamic ripple pulse, call duration, and status.
- **Control Bar:** A pill‑shaped, draggable row containing Mute, Speaker, Keypad, Add Call, and Record buttons. Can be dragged to the bottom-left/right for one‑hand use.
- **Record Button:** Distinct icon (circle/dot) that, when activated, spawns a red glowing ring and a small waveform chip at the top.

### Lists (Recents, Contacts)
- **Item:** Transparent background, subtle left accent line for missed calls, swipe-to-reveal actions (call, delete) with bounce animation.
- **Scrolling:** Overscroll effect uses a stretch/bounce instead of Material’s edge glow.
- **Empty States:** Minimalist illustration plus a concise message, no heavy graphics.

### Sheets & Dialogs
- **Bottom Sheets:** Rounded top corners (24dp), drag handle, dynamic height, background blur. Content slides in with spring.
- **Alerts:** Centered, glassmorphism style, not the default Material dialog. Use custom composable.

## 4. Animation Language
All animations use `androidx.compose.animation` with custom springs and tween specs.

- **Default Spring:** `spring(dampingRatio = 0.6f, stiffness = 400f)` for interactive elements.
- **Duration Tokens:** Micro (150ms), Small (250ms), Medium (350ms), Large (500ms).
- **Shared Element Transitions:** Custom `Modifier.sharedElement()` and `graphicsLayer` for morphing avatar from contact list to in-call screen.
- **Recording Indicator:** Pulsing red glow using `InfiniteTransition` on scale and alpha, synced to a mock audio level (while recording).
- **Call Connect:** Haptic “thud” + expanding circular ripple from avatar.
- **Number Entry:** Each digit scales up 1.2x and down with overshoot.
- **Page Transitions:** Tab content fades in/out with a 15% opacity overlap, no sliding.

## 5. Layout & Responsiveness
- **Primary Navigation:** Bottom tab bar (3–4 items). Height 64dp, icons centered with labels underneath.
- **One‑Handed Optimisation:** On large screens, shift the dialpad and in‑call controls toward the bottom half using a top‑margin that adapts to screen height.
- **Tablet/Foldable:** Two‑pane layout: Recents list on left, detail/dialpad on right, using `WindowSizeClass`.

## 6. Dark Mode / Themes
- Automatically follows system setting, with an in‑app toggle that respects battery saver.
- Colors flip without animation glitches; all components derive colors from a custom `DialerColorScheme` (not `MaterialTheme.colors`).

## 7. Accessibility
- Content descriptions on all icons and buttons.
- Minimum touch target 48dp, properly merged with `Modifier.clickable` semantics.
- Haptic feedback synchronized with visual events.
- Font scaling respected throughout (no forced line height clipping).