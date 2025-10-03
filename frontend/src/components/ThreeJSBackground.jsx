import React, { useEffect, useRef } from 'react'
import * as THREE from 'three'

/**
 * Three.js Background Component with Flying Books
 * Componente Sfondo Three.js con Libri Volanti
 * 
 * Creates a 3D animated background with floating books and particles
 * Crea uno sfondo 3D animato con libri fluttuanti e particelle
 */
const ThreeJSBackground = () => {
  const mountRef = useRef(null)
  const sceneRef = useRef(null)
  const rendererRef = useRef(null)
  const animationRef = useRef(null)

  useEffect(() => {
    if (!mountRef.current) return

    // Scene setup / Configurazione scena
    const scene = new THREE.Scene()
    sceneRef.current = scene

    // Camera setup / Configurazione camera
    const camera = new THREE.PerspectiveCamera(
      75,
      window.innerWidth / window.innerHeight,
      0.1,
      1000
    )
    camera.position.z = 8

    // Renderer setup / Configurazione renderer
    const renderer = new THREE.WebGLRenderer({ 
      alpha: true, 
      antialias: true 
    })
    renderer.setSize(window.innerWidth, window.innerHeight)
    renderer.setClearColor(0x000000, 0)
    rendererRef.current = renderer
    mountRef.current.appendChild(renderer.domElement)

    // Create floating particles / Crea particelle fluttuanti
    const particlesGeometry = new THREE.BufferGeometry()
    const particlesCount = 2000
    const positions = new Float32Array(particlesCount * 3)

    for (let i = 0; i < particlesCount * 3; i++) {
      positions[i] = (Math.random() - 0.5) * 30
    }

    particlesGeometry.setAttribute('position', new THREE.BufferAttribute(positions, 3))

    const particlesMaterial = new THREE.PointsMaterial({
      size: 0.03,
      color: 0x3b82f6,
      transparent: true,
      opacity: 0.8
    })

    const particles = new THREE.Points(particlesGeometry, particlesMaterial)
    scene.add(particles)

    // Create floating books / Crea libri fluttuanti
    const books = []
    const bookColors = [
      0x8B4513, // Brown
      0x2E8B57, // Sea Green
      0x4169E1, // Royal Blue
      0xDC143C, // Crimson
      0xFF8C00, // Dark Orange
      0x9932CC, // Dark Orchid
      0x20B2AA, // Light Sea Green
      0xFF6347, // Tomato
    ]

    for (let i = 0; i < 40; i++) {
      // Create book geometry / Crea geometria libro
      const bookGeometry = new THREE.BoxGeometry(0.4, 0.5, 0.08)
      const bookMaterial = new THREE.MeshLambertMaterial({
        color: bookColors[Math.floor(Math.random() * bookColors.length)],
        transparent: true,
        opacity: 0.8
      })
      const book = new THREE.Mesh(bookGeometry, bookMaterial)
      
      // Random position / Posizione casuale
      book.position.x = (Math.random() - 0.5) * 30
      book.position.y = (Math.random() - 0.5) * 30
      book.position.z = (Math.random() - 0.5) * 20
      
      // Random rotation / Rotazione casuale
      book.rotation.x = Math.random() * Math.PI
      book.rotation.y = Math.random() * Math.PI
      book.rotation.z = Math.random() * Math.PI
      
      // Add book title / Aggiungi titolo libro
      const titleGeometry = new THREE.PlaneGeometry(0.3, 0.12)
      const titleMaterial = new THREE.MeshBasicMaterial({
        color: 0xFFFFFF,
        transparent: true,
        opacity: 0.9
      })
      const title = new THREE.Mesh(titleGeometry, titleMaterial)
      title.position.z = 0.05
      book.add(title)
      
      // Add book spine / Aggiungi dorso libro
      const spineGeometry = new THREE.BoxGeometry(0.05, 0.5, 0.08)
      const spineMaterial = new THREE.MeshLambertMaterial({
        color: 0x2C3E50,
        transparent: true,
        opacity: 0.8
      })
      const spine = new THREE.Mesh(spineGeometry, spineMaterial)
      spine.position.x = -0.175
      book.add(spine)
      
      scene.add(book)
      books.push({
        mesh: book,
        originalY: book.position.y,
        originalX: book.position.x,
        originalZ: book.position.z,
        speed: Math.random() * 0.03 + 0.01,
        rotationSpeed: Math.random() * 0.03 + 0.01
      })
    }

    // Create floating knowledge symbols / Crea simboli di conoscenza fluttuanti
    const symbols = []
    for (let i = 0; i < 15; i++) {
      const symbolGeometry = new THREE.TorusGeometry(0.1, 0.03, 8, 16)
      const symbolMaterial = new THREE.MeshBasicMaterial({
        color: new THREE.Color().setHSL(Math.random(), 0.8, 0.6),
        transparent: true,
        opacity: 0.6
      })
      const symbol = new THREE.Mesh(symbolGeometry, symbolMaterial)
      
      symbol.position.x = (Math.random() - 0.5) * 20
      symbol.position.y = (Math.random() - 0.5) * 20
      symbol.position.z = (Math.random() - 0.5) * 10
      
      scene.add(symbol)
      symbols.push(symbol)
    }

    // Add lighting / Aggiungi illuminazione
    const ambientLight = new THREE.AmbientLight(0x404040, 0.6)
    scene.add(ambientLight)

    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8)
    directionalLight.position.set(5, 5, 5)
    scene.add(directionalLight)

    // Animation loop / Loop di animazione
    const animate = () => {
      animationRef.current = requestAnimationFrame(animate)

      // Rotate particles / Ruota particelle
      particles.rotation.x += 0.001
      particles.rotation.y += 0.002

      // Animate books / Anima libri
      books.forEach((book, index) => {
        book.mesh.rotation.x += book.rotationSpeed
        book.mesh.rotation.y += book.rotationSpeed * 0.5
        book.mesh.rotation.z += book.rotationSpeed * 0.3
        
        // Floating motion / Movimento fluttuante
        book.mesh.position.y = book.originalY + Math.sin(Date.now() * 0.001 + index) * 3
        book.mesh.position.x = book.originalX + Math.sin(Date.now() * 0.0008 + index) * 2
        book.mesh.position.z = book.originalZ + Math.cos(Date.now() * 0.0012 + index) * 1.5
        
        // Scale pulsing / Pulsazione scala
        const scale = 1 + Math.sin(Date.now() * 0.002 + index) * 0.15
        book.mesh.scale.set(scale, scale, scale)
      })

      // Animate symbols / Anima simboli
      symbols.forEach((symbol, index) => {
        symbol.rotation.x += 0.01
        symbol.rotation.y += 0.01
        symbol.position.x += Math.sin(Date.now() * 0.001 + index) * 0.005
        symbol.position.y += Math.cos(Date.now() * 0.0015 + index) * 0.005
        symbol.position.z += Math.sin(Date.now() * 0.0008 + index) * 0.003
      })

      // Rotate camera slightly / Ruota leggermente la camera
      camera.position.x = Math.sin(Date.now() * 0.0003) * 1
      camera.position.y = Math.cos(Date.now() * 0.0002) * 0.5
      camera.lookAt(0, 0, 0)

      renderer.render(scene, camera)
    }

    animate()

    // Handle window resize / Gestisce ridimensionamento finestra
    const handleResize = () => {
      camera.aspect = window.innerWidth / window.innerHeight
      camera.updateProjectionMatrix()
      renderer.setSize(window.innerWidth, window.innerHeight)
    }

    window.addEventListener('resize', handleResize)

    // Cleanup / Pulizia
    return () => {
      window.removeEventListener('resize', handleResize)
      if (animationRef.current) {
        cancelAnimationFrame(animationRef.current)
      }
      if (mountRef.current && renderer.domElement) {
        mountRef.current.removeChild(renderer.domElement)
      }
      renderer.dispose()
    }
  }, [])

  return (
    <div 
      ref={mountRef} 
      style={{
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        zIndex: -1,
        pointerEvents: 'none'
      }}
    />
  )
}

export default ThreeJSBackground
