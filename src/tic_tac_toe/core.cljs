(ns tic-tac-toe.core
  (:require
   [reagent.core :as reagent :refer [atom]]
   ;[goog.dom :as dom]
   ;;[goog.Timer :as Timer]
   ;;[clojure.math.numeric-tower :as math]
   ;;[clojure.math.combinatorics :as combo]
   )
  ;;(:import [goog.fx.dom FadeInAndShow])
  )

(enable-console-print!)


(def title (atom "Tic Tac Toe"))

(def board-state (reagent/atom {:sq [0,0,0,0,0,0,0,0,0] :who-go 1 :winner false}))

;; recap javascript mouse events 
;; function clickEvent(e) {
;;   // e = Mouse click event.
;;   var rect = e.target.getBoundingClientRect();
;;   var x = e.clientX - rect.left; //x position within the element.
;;   var y = e.clientY - rect.top;  //y position within the element.
;; }


;; (list
;;  [0 1 2]
;;  [3 4 5]
;;  [6 7 8]
;;  [0 3 6]
;;  [1 4 7]
;;  [2 5 8]
;;  [0 4 8]
;;  [2 4 6])

(defn check-for-winner [sq a b c]
  (and (not (= (sq a) 0))
       (= (sq a) (sq b) (sq c))))


(defn any-winners [sq]
  
   false
    
;; 0 1 2
;; 3 4 5
;; 6 7 8
;; 0 3 6
;; 1 4 7
;; 2 5 8
;; 0 4 8
;; 2 4 6
  
  
  )




(defn you-clicked [ev]
  (let [rect (-> ev .-target .getBoundingClientRect)
        cx (.-clientX ev)
        cy (.-clientY ev)]
    (let [rleft (.-left rect)
          rtop (.-top rect)
          rright (.-right rect)
          rbot (.-bottom rect)]
      (let [dx (- cx rleft)
            dy (- cy rtop)]
        
        (console.log "event client.x = " cx  " , client.y=" cy)
        (console.log "bounding client rectangle  rect.left = " rleft
                     " rect.top=" rtop
                     " rect.right=" rright
                     " rect.bottom=" rbot)
        (console.log "Important result --->> dx = " dx " , dy=" dy)
        ))))


;; change board-state with next player
(defn next-player [p]
  (if
      (= p 1)
    2
    1))


(defn make-square-clickable [x y s bgcolor sq player]  
  (list
   [:rect {:x x
           :y y
           :width "1"
           :height "1"
           :style {:fill bgcolor}
           :on-click (fn [ev]
                       (reset! board-state {:sq (assoc sq s player)  :who-go (next-player player)})
                       (console.log "you clicked on my square x,y= " x " , " y )
                       )
           }]
   ))



(defn make-square-occupied [x y s bgcolor player]
  (list
   [:rect {:x x
           :y y
           :width "1"
           :height "1"
           :style {:fill bgcolor}           
           }]
   ;; draw a circle if player = 1 of certain color
   ;; draw a circle if player = 2 of different color   
   [:circle {:cx (+ x 0.5) :cy (+ y 0.5) :r 0.45 :style {:stroke "green" :stroke-width "0.01" :fill
                                                        (if (= player 1)
                                                          "yellow"
                                                          "black")}} ]
   ))


(defn make-square-player-1 [x y s bgcolor]
  (make-square-occupied x y s bgcolor 1))

(defn make-square-player-2 [x y s bgcolor]
  (make-square-occupied x y s bgcolor 2))


(defn make-square [x y s bgcolor]
  (let [sq (get @board-state :sq)]
    (let [player (get @board-state :who-go)]
      (cond
        ;; its an empty square - when clicked places a 1 or a 2 at that location of board-state
        (= (sq s) 0) (make-square-clickable x y s bgcolor sq player)
        (= (sq s) 1) (make-square-player-1 x y s bgcolor)
        true         (make-square-player-2 x y s bgcolor)))))


              

;; if 0 then empty square and if clicked based on current board state , place that players counter there ,
;; update board state
;; if true or false - already a counter there , 

;; has there been a winner , if so do not act on any mouse events ??


(defn line-1 []
   ;; horz divider 1
  [:rect {:x "0"  :y "1"  :width "3" :height "0.01" :style {:fill "black"}   } ] )

(defn line-2 []
   ;; horz divider 2
   [:rect {:x "0"  :y "2"  :width "3" :height "0.01" :style {:fill "black"}   } ])

(defn line-3 []
   ;; vert divider 1
  [:rect {:x "1"  :y "0"  :width "0.01" :height "3" :style {:fill "black"}   } ])

(defn line-4 []
   ;; vert divider 2
  [:rect {:x "2"  :y "0"  :width "0.01" :height "3" :style {:fill "black"}   } ])


(defn four-lines []
  (list
   [line-1]
   [line-2]
   [line-3]
   [line-4]))

(defn lots-horizontal-lines []
  (into
   []
   (for [y (range -0.01 3.3 0.2)]
    [:rect {:x "0"
            :y y
            :width "3"
            :height "0.01"
            :style {:fill "black"}}])))


(defn lots-vertical-lines []
  (into
   []
   (for [x (range -0.01 3.3 0.2)]
    [:rect {:x x
            :y "0"
            :width "0.01"
            :height "3"
            :style {:fill "black"}}])))


;; 
;;
;; dev:cljs.user=> (into [1 2 3] '(:a :b))
;; [1 2 3 :a :b]
;; dev:cljs.user=> (into (into [1 2 3] '(:a :b)) '(:c :d))
;; [1 2 3 :a :b :c :d]
;; dev:cljs.user=> (into (into (into [1 2 3] '(:a :b)) '(:c :d)) '(:e :f))
;; [1 2 3 :a :b :c :d :e :f]
;; dev:cljs.user=> (into (into (into (into [1 2 3] '(:a :b)) '(:c :d)) '(:e :f)) '(:g :h))
;; [1 2 3 :a :b :c :d :e :f :g :h]



;;[make-square 0 0 0 "blue"]
(defn tic-tac-toe-board []
  [:div {:class "game" :align "center"}
   (reduce into
           [:svg {:width "500" :height "500" :viewBox "0 0 3 3" :style {:margin "none"}}]           
           (list

            (make-square 0 0 0 "blue")
            (make-square 1 0 1 "green")
            (make-square 2 0 2 "blue")
            
            (make-square 0 1 3 "green")
            (make-square 1 1 4 "blue")
            (make-square 2 1 5 "green")

            (make-square 0 2 6 "blue")
            (make-square 1 2 7 "green")
            (make-square 2 2 8 "blue")

            (four-lines)
            
            ))])


     ;; (make-square 1 0 1 "green")
     ;; (make-square 2 0 2 "blue")
     
     ;; (make-square 0 1 3 "green")
     ;; (make-square 1 1 4 "blue")
     ;; (make-square 2 1 5 "green")
     
     ;; (make-square 0 2 6 "blue")
     ;; (make-square 1 2 7 "green")
     ;; (make-square 2 2 8 "blue")
     

    ;;[lines]

    ;; [:rect {:x "1"  :y "0" :width "1" :height "1" :style {:fill "green"}  :on-click (fn [ev] (console.log "square 2 clicked" )) } ]
    ;; [:rect {:x "2"  :y "0" :width "1" :height "1" :style {:fill "blue"}  :on-click (fn [ev] (console.log "square 3 clicked" )) } ]

    ;; ;; row 2
    ;; [:rect {:x "0"  :y "1" :width "1" :height "1" :style {:fill "green"}  :on-click (fn [ev] (console.log "square 4 clicked" )) } ]
    ;; [:rect {:x "1"  :y "1" :width "1" :height "1" :style {:fill "blue"}  :on-click (fn [ev] (console.log "square 5 clicked" )) } ]
    ;; [:rect {:x "2"  :y "1" :width "1" :height "1" :style {:fill "black"}  :on-click (fn [ev] (console.log "square 6 clicked" )) } ]

    ;; ;; row 3
    ;; [:rect {:x "0"  :y "2" :width "1" :height "1" :style {:fill "blue"}  :on-click (fn [ev] (console.log "square 7 clicked" )) } ]
    ;; [:rect {:x "1"  :y "2" :width "1" :height "1" :style {:fill "black"}  :on-click (fn [ev] (console.log "square  8 clicked" )) } ]
    ;; [:rect {:x "2"  :y "2" :width "1" :height "1" :style {:fill "green"}  :on-click (fn [ev] (console.log "square 9 clicked" )) } ]




    
    ;; [:circle {:cx "50" :cy "50" :r "40" :style {:stroke "green" :stroke-width "4" :fill "yellow"}} ]
    ;; [:circle {:cx "150" :cy "50" :r "40" :style {:stroke "green" :stroke-width "4" :fill "blue"}} ]
    ;; [:circle {:cx "250" :cy "50" :r "40" :style {:stroke "green" :stroke-width "4" :fill "yellow"}} ]
    ;; [:circle {:cx "50" :cy "150" :r "40" :style {:stroke "green" :stroke-width "4" :fill "blue"
    ;;                                              :opacity "0.5"}} ]
    ;; [:circle {:cx "150" :cy "150" :r "40" :style {:stroke "green" :stroke-width "4" :fill "yellow"}} ]
    ;; [:circle {:cx "250" :cy "150" :r "40" :style {:stroke "green" :stroke-width "4" :fill "blue"}} ]
    ;; [:circle {:cx "50" :cy "250" :r "40" :style {:stroke "green" :stroke-width "4" :fill "yellow"}} ]
    ;; (when false
    ;;   [:circle {:cx "150" :cy "250" :r "40" :style {:stroke "green" :stroke-width "4" :fill "blue"}} ]
    ;;   )
    ;; (when true
    ;;   [:circle {:cx "250" :cy "250" :r "40" :style {:stroke "green" :stroke-width "4" :fill "yellow"
    ;;                                                 :opacity "0.5"}
    ;;             :on-mouse-move #(console.log "hello world! x = " (.-clientX %) " y="(.-clientY %))
    ;;             :on-mouse-enter #(console.log "mouse entered !  ")
    ;;             :on-mouse-leave #(console.log "mouse left!! x = ")
    ;;             :on-mouse-over #(console.log "mouse over!! x = ")
    ;;             } ])

 
  
  
(defn new-game []
  (reset! board-state (reagent/atom {:sq [0,0,0,0,0,0,0,0,0] :who-go 1 :winner false})))

(defn tic-tac-toe []
  [:div {:class "clock"}
   [:h1 {:style {:text-align "center" } } @title]
   
   [tic-tac-toe-board]
   [:div {:id "newGame" :align "center"}
    [:button {:on-click (fn [ev]
                          [new-game]
                          (console.log "should be a new game !")
                          )
              } "New Game" ]
    ]
   
   
   ;;[:img {:id "img1"           :src "img1.jpg"           :style {:visibility "shown" :opacity @opacity}}] 
   [:div {:id "div3"}
    ;;[:img {:id "img3" :src "img3.jpg" :style {:visibility "shown"}}]
    ]
   ])



(reagent/render-component [tic-tac-toe]
                          (. js/document (getElementById "app")))


(set!  (.-bgColor js/document) "red")


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

